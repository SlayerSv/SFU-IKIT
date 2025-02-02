package pr4

import (
	"testing"
)

func TestRookGoToPosition(t *testing.T) {
	var tests = map[string]struct {
		from               string
		to                 string
		pieceSide          side
		hasOtherPiece      bool
		otherPieceSide     side
		otherPiecePosition string
		wantErr            bool
	}{
		"move top":              {"a1", "a8", WHITE, false, BLACK, "", false},
		"move left":             {"h4", "a4", WHITE, false, BLACK, "", false},
		"move right":            {"a5", "h5", WHITE, false, BLACK, "", false},
		"move down":             {"e8", "e1", WHITE, false, BLACK, "", false},
		"move diagonal":         {"c8", "a6", WHITE, false, BLACK, "", true},
		"not straight move":     {"g3", "d2", WHITE, false, BLACK, "", true},
		"move to same field":    {"f6", "f6", WHITE, false, BLACK, "", true},
		"take":                  {"d4", "c4", WHITE, true, BLACK, "c4", false},
		"to field blocked":      {"b5", "b8", WHITE, true, WHITE, "b8", true},
		"path blocked by enemy": {"d7", "d1", WHITE, true, BLACK, "d4", true},
		"path blocked by ally":  {"b2", "f2", WHITE, true, WHITE, "d2", true},
		"out of bounds":         {"a1", "a9", WHITE, false, BLACK, "", true},
	}
	for tname, tt := range tests {
		var board = NewChessBoard()
		fromPos, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("from position is incorrect %s", tt.from)
			continue
		}
		rook := NewChessPiece(ROOK, tt.pieceSide, board.GetField(fromPos))
		var otherPiece IChessPiece
		otherPiecePos, err := NewChessBoardPosition(tt.otherPiecePosition)
		if tt.hasOtherPiece {
			if err != nil {
				t.Fatalf("other piece field position is incorrect %s", tt.otherPiecePosition)
				continue
			}
			otherPiece = NewChessPiece(ROOK, tt.otherPieceSide, board.GetField(otherPiecePos))
		}
		t.Run(tname, func(t *testing.T) {
			fromField := board.GetField(fromPos)
			_, err := rook.GoToPosition(tt.to, board)

			// process error case, meaning the move is illegal and
			// all pieces should stay on the same fields
			if tt.wantErr {
				if err == nil {
					t.Errorf("expected an error")
				}

				if fromField.GetChessPiece() != rook {
					t.Errorf("rook didnt stay on field %s", tt.from)
				}
				if rook.GetChessField() != fromField {
					t.Errorf("rook field is incorrect got %s want %s",
						rook.GetChessField().GetPosition().String(), tt.from)
				}
				if !tt.hasOtherPiece {
					return
				}
				otherPieceField := board.GetField(otherPiecePos)
				if otherPieceField.GetChessPiece() != otherPiece {
					t.Errorf("other piece didnt stay on field %s", tt.otherPiecePosition)
				}
				if otherPiece.GetChessField() != otherPieceField {
					t.Errorf("other piece field is incorrect got %s want %s",
						otherPiece.GetChessField().GetPosition().String(), tt.otherPiecePosition)
				}
				return
			}

			// legal move
			if err != nil {
				t.Errorf("unexpected error: %v", err)
			}
			toPos, err := NewChessBoardPosition(tt.to)
			if err != nil {
				t.Fatalf("to position is incorrect %s", tt.to)
			}
			toField := board.GetField(toPos)

			if fromField.GetChessPiece() != nil {
				t.Errorf("rook didnt move from field %s", tt.from)
			}
			if toField.GetChessPiece() != rook {
				t.Errorf("rook didnt move to field %s", tt.to)
			}
			if rook.GetChessField() != toField {
				t.Errorf("rook's chess field is incorrect: got %s, want %s",
					rook.GetChessField().GetPosition().String(),
					toField.GetPosition().String())
			}
		})
	}
}
