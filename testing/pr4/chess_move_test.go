package pr4

import (
	"testing"
)

func TestChessMoveString(t *testing.T) {
	tests := []struct {
		from      string
		to        string
		pieceType chessPieceType
		isTake    bool
		isCheck   bool
		isMate    bool
		want      string
	}{
		{"a1", "a2", KING, false, false, false, "Ka1a2"},
		{"b2", "b3", ROOK, false, false, false, "Rb2b3"},
		{"e7", "e6", KING, true, false, false, "Ke7xe6"},
		{"g1", "h1", ROOK, false, true, false, "Rg1h1+"},
		{"d2", "c2", ROOK, true, true, false, "Rd2xc2+"},
		{"a1", "a2", ROOK, false, false, true, "Ra1a2#"},
		{"a1", "a2", ROOK, true, false, true, "Ra1xa2#"},
		{"e1", "c1", KING, false, false, false, "0-0-0"},
		{"e8", "c8", KING, false, false, false, "0-0-0"},
		{"e1", "g1", KING, false, false, false, "0-0"},
		{"e8", "g8", KING, false, false, false, "0-0"},
	}
	for _, tt := range tests {
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("inocrrect position %s", tt.from)
		}
		pTo, err := NewChessBoardPosition(tt.to)
		if err != nil {
			t.Fatalf("inocrrect position %s", tt.to)
		}
		cm := chessMove{pFrom, pTo, tt.pieceType, tt.isTake, tt.isCheck, tt.isMate}
		t.Run(tt.want, func(t *testing.T) {
			if cm.String() != tt.want {
				t.Errorf("wrong chess move notation got %s want %s",
					cm.String(), tt.want)
			}
		})
	}
}
