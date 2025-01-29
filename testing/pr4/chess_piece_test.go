package pr4

import "testing"

func TestPiece(t *testing.T) {
	tests := []struct {
		cpt  chessPieceType
		want string
	}{
		{KING, "K"},
		{ROOK, "R"},
		{PAWN, ""},
	}
	NewChessPiece(PAWN, WHITE, nil)
	for _, tt := range tests {
		t.Run(tt.want, func(t *testing.T) {
			if tt.cpt.String() != tt.want {
				t.Errorf("got %s want %s", tt.cpt.String(), tt.want)
			}
		})
	}

}
