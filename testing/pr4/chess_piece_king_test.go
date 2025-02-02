package pr4

import (
	"testing"
)

func TestKingGoToPosition(t *testing.T) {
	var tests = map[string]struct {
		from               string
		to                 string
		kingSide           side
		hasOtherPiece      bool
		otherPieceSide     side
		otherPiecePosition string
		toUnderCheck       bool
		wantErr            bool
	}{
		"move top":               {"a1", "a2", WHITE, false, BLACK, "", false, false},
		"move top-left":          {"c7", "b8", WHITE, false, BLACK, "", false, false},
		"move top-right":         {"c7", "d8", WHITE, false, BLACK, "", false, false},
		"move down":              {"e8", "e7", WHITE, false, BLACK, "", false, false},
		"move down-left":         {"e8", "d7", WHITE, false, BLACK, "", false, false},
		"move down-right":        {"d6", "e5", WHITE, false, BLACK, "", false, false},
		"move left":              {"h4", "g4", WHITE, false, BLACK, "", false, false},
		"move right":             {"a5", "b5", WHITE, false, BLACK, "", false, false},
		"move 2 rows away":       {"g3", "g5", WHITE, false, BLACK, "", false, true},
		"move 2 cols away":       {"b4", "d4", WHITE, false, BLACK, "", false, true},
		"move to same field":     {"f6", "f6", WHITE, false, BLACK, "", false, true},
		"move to under check":    {"a1", "a2", WHITE, false, BLACK, "", true, true},
		"take as white":          {"d4", "c4", WHITE, true, BLACK, "c4", false, false},
		"take as black":          {"d4", "c4", BLACK, true, WHITE, "c4", false, false},
		"take under check":       {"d4", "c4", WHITE, true, BLACK, "c4", true, true},
		"to field blocked white": {"b5", "b6", WHITE, true, WHITE, "b6", false, true},
		"to field blocked black": {"b5", "b6", BLACK, true, BLACK, "b6", false, true},
		"out of bounds":          {"a1", "a9", WHITE, false, BLACK, "", false, true},
	}
	for tname, tt := range tests {
		var board = NewChessBoard()
		fromPos, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("from position is incorrect %s", tt.from)
			continue
		}
		toPos, err := NewChessBoardPosition(tt.to)
		if tt.toUnderCheck {
			if err != nil {
				t.Fatalf("to position is incorrect %s", tt.from)
			}
			board.GetField(toPos).SetAttackedBy(tt.kingSide.GetOpposite())
		}

		king := NewChessPiece(KING, tt.kingSide, board.GetField(fromPos))
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
			_, err := king.GoToPosition(tt.to, board)

			// process error case, meaning the move is illegal and
			// all pieces should stay on the same fields
			if tt.wantErr {
				if err == nil {
					t.Errorf("expected an error")
				}
				if fromField.GetChessPiece() != king {
					t.Errorf("king didnt stay on field %s", tt.from)
				}
				if king.GetChessField() != fromField {
					t.Errorf("king field is incorrect got %s want %s",
						king.GetChessField().GetPosition().String(), tt.from)
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
				t.Errorf("king didnt move from field %s", tt.from)
			}
			if toField.GetChessPiece() != king {
				t.Errorf("king didnt move to field %s", tt.to)
			}
			if king.GetChessField() != toField {
				t.Errorf("king's chess field is incorrect: got %s, want %s",
					king.GetChessField().GetPosition().String(),
					toField.GetPosition().String())
			}
		})
	}
}
func TestKingCastle(t *testing.T) {
	var tests = map[string]struct {
		from        string
		to          string
		kingSide    side
		kingMoved   bool
		hasRook     bool
		rookSide    side
		rookMoved   bool
		blocked     bool
		underAttack bool
		wantErr     bool
	}{
		"white kingside castle":  {"d1", "b1", WHITE, false, true, WHITE, false, false, false, false},
		"white queenside castle": {"d1", "f1", WHITE, false, true, WHITE, false, false, false, false},
		"black kingside castle":  {"d8", "b8", BLACK, false, true, BLACK, false, false, false, false},
		"black queenside castle": {"d8", "f8", BLACK, false, true, BLACK, false, false, false, false},
		"Rook diffent color":     {"d1", "b1", WHITE, false, true, BLACK, false, false, false, true},
		"king moved":             {"d1", "b1", WHITE, true, true, WHITE, false, false, false, true},
		"rook moved":             {"d1", "b1", WHITE, false, true, WHITE, true, false, false, true},
		"blocked by a piece":     {"d1", "b1", WHITE, false, true, WHITE, false, true, false, true},
		"field under attack":     {"d1", "b1", WHITE, false, true, WHITE, false, false, true, true},
		"no rook":                {"d8", "f8", BLACK, false, false, BLACK, false, false, false, true},
	}

	for testName, tt := range tests {
		var board = NewChessBoard()
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		pTo, err := NewChessBoardPosition(tt.to)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		king := NewChessPiece(KING, tt.kingSide, board.GetField(pFrom)).(IChessPieceCastler)
		var rook IChessPiece
		rookPos := pTo
		if tt.hasRook {
			if rookPos.GetCol() > pFrom.GetCol() {
				rookPos.SetCol('h')
			} else {
				rookPos.SetCol('a')
			}
			rook = NewChessPiece(ROOK, tt.rookSide, board.GetField(rookPos))
			if tt.rookMoved {
				rooks := rook.(IChessPieceCastler)
				rooks.SetMoved()
			}
		}
		if tt.kingMoved {
			king.SetMoved()
			if king.HasMoved() != true {
				t.Errorf("king didnt change moved status")
			}
		}
		if tt.underAttack {
			board.GetField(pTo).SetAttackedBy(king.GetSide().GetOpposite())
		}
		if tt.blocked {
			pos := pFrom
			pos.SetCol(pFrom.GetCol() + 1)
			field := board.GetField(pos)
			field.SetChessPiece(NewChessPiece(ROOK, WHITE, field))
			pos.SetCol(pFrom.GetCol() - 1)
			field = board.GetField(pos)
			field.SetChessPiece(NewChessPiece(ROOK, WHITE, field))
		}
		t.Run(testName, func(t *testing.T) {
			_, err = king.GoToPosition(tt.to, board)
			if tt.wantErr {
				if err == nil {
					t.Errorf("expected an error")
				}
				fromField := board.GetField(pFrom)
				if fromField.GetChessPiece() != king {
					t.Errorf("king didnt stay on field %s", tt.from)
				}
				if king.GetChessField() != fromField {
					t.Errorf("king field is incorrect got %s want %s",
						king.GetChessField().GetPosition().String(), tt.from)
				}
				if !tt.hasRook {
					return
				}
				rookField := board.GetField(rookPos)
				if rookField.GetChessPiece() != rook {
					t.Errorf("rook didnt stay on field %s", rookPos.String())
				}
				if rook.GetChessField() != rookField {
					t.Errorf("rook field is incorrect got %s want %s",
						rook.GetChessField().GetPosition().String(), rookPos.String())
				}
				return
			}
			if err != nil {
				t.Errorf("unexpected error %v", err)
			}
			if board.GetField(pFrom).GetChessPiece() == king {
				t.Errorf("king stayed on field %s", tt.from)
			}
			if board.GetField(pTo).GetChessPiece() != king {
				t.Errorf("king didnt move to field %s", tt.to)
			}
			if king.GetChessField() != board.GetField(pTo) {
				t.Errorf("king's chess field is incorrect: got %s, want %s",
					king.GetChessField().GetPosition().String(),
					board.GetField(pTo).GetPosition().String())
			}
			if board.GetField(rookPos).GetChessPiece() != nil {
				t.Errorf("rook stayed on field %s", rookPos.String())
			}
			if pTo.GetCol() > pFrom.GetCol() {
				rookPos.SetCol('e')
			} else {
				rookPos.SetCol('c')
			}
			if board.GetField(rookPos).GetChessPiece() != rook {
				t.Errorf("rook didnt move to field %s", rookPos.String())
			}
			if rook.GetChessField() != board.GetField(rookPos) {
				t.Errorf("rook chess field is incorrect: got %s, want %s",
					rook.GetChessField().GetPosition().String(),
					board.GetField(rookPos).GetPosition().String())
			}
		})
	}
}
