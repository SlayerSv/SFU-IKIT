package pr4

import (
	"fmt"
	"testing"
)

func TestRookMove(t *testing.T) {
	var tests = []struct {
		from    string
		to      string
		wantErr bool
	}{
		{"d4", "d5", false},
		{"a1", "h1", false},
		{"d4", "d8", false},
		{"b7", "b2", false},
		{"d4", "d4", true},
		{"c4", "d3", true},
		{"d4", "j1", true},
		{"c3", "asd", true},
	}
	for _, tt := range tests {
		var board = NewChessBoard()
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("incorrect position: %v, pos: %s", err, tt.from)
		}
		rook := NewChessPiece(ROOK, WHITE, board.GetField(pFrom))
		t.Run(fmt.Sprintf("%s%s%s", rook.GetSide(), tt.from, tt.to), func(t *testing.T) {
			cm, err := rook.GoToPosition(tt.to, board)
			if err != nil && !tt.wantErr {
				t.Errorf("unexpected error: %v", err)
			}
			if err == nil && tt.wantErr {
				t.Errorf("expected an error")
			}
			if err == nil {
				if cm.String() != fmt.Sprintf("R%s%s", tt.from, tt.to) {
					t.Errorf("wrong move notation got %s want %s",
						cm.String(), fmt.Sprintf("R%s%s", tt.from, tt.to))
				}
			}
		})
	}
}

func TestRookTake(t *testing.T) {
	var tests = []struct {
		from           string
		to             string
		rookSide       side
		otherPieceSide side
		wantErr        bool
	}{
		{"d4", "b4", WHITE, BLACK, false},
		{"a1", "h1", BLACK, WHITE, false},
		{"e4", "e8", WHITE, WHITE, true},
		{"b7", "b2", BLACK, BLACK, true},
	}
	for _, tt := range tests {
		var board = NewChessBoard()
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("incorrect position: %v, pos: %s", err, tt.from)
		}
		pTo, err := NewChessBoardPosition(tt.to)
		if err != nil {
			t.Fatalf("incorrect position: %v, pos: %s", err, tt.to)
		}
		rook := NewChessPiece(ROOK, tt.rookSide, board.GetField(pFrom))
		NewChessPiece(ROOK, tt.otherPieceSide, board.GetField(pTo))
		t.Run(fmt.Sprintf("%s%s%s", rook.GetSide(), tt.from, tt.to), func(t *testing.T) {
			cm, err := rook.GoToPosition(tt.to, board)
			if tt.wantErr && err == nil {
				t.Errorf("expected an error")
			}
			if !tt.wantErr && err != nil {
				t.Errorf("unexpected error: %v", err)
			}
			if err == nil {
				if cm.String() != fmt.Sprintf("R%sx%s", tt.from, tt.to) {
					t.Errorf("wrong move notation got %s want %s",
						cm.String(), fmt.Sprintf("R%sx%s", tt.from, tt.to))
				}
			}
		})
	}
}

func TestRookBlockedMove(t *testing.T) {
	var tests = []struct {
		from     string
		to       string
		blockPos string
	}{
		{"d4", "d7", "d5"},
		{"a1", "h1", "g1"},
		{"e4", "e8", "e6"},
		{"c7", "a7", "b7"},
	}
	for _, tt := range tests {
		var board = NewChessBoard()
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("incorrect position: %v, pos: %s", err, tt.from)
		}
		pBlock, err := NewChessBoardPosition(tt.blockPos)
		if err != nil {
			t.Fatalf("incorrect position: %v, pos: %s", err, tt.blockPos)
		}
		piece := NewChessPiece(ROOK, WHITE, board.GetField(pFrom))
		NewChessPiece(ROOK, BLACK, board.GetField(pBlock))
		t.Run(fmt.Sprintf("%s%s%s", piece.GetSide(), tt.from, tt.to), func(t *testing.T) {
			_, err := piece.GoToPosition(tt.to, board)
			if err == nil {
				t.Errorf("expected an error")
			}
		})
	}
}
