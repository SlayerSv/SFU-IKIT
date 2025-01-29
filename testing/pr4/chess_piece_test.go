package pr4

import "testing"

func TestChessPieceType(t *testing.T) {
	tests := []struct {
		cpt  chessPieceType
		want string
	}{
		{KING, "K"},
		{ROOK, "R"},
		{PAWN, ""},
		{KNIGHT, "N"},
		{BISHOP, "B"},
		{QUEEN, "Q"},
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

func TestSide(t *testing.T) {
	if WHITE.GetOpposite() != BLACK {
		t.Errorf("wrong opposite of white")
	}
	if BLACK.GetOpposite() != WHITE {
		t.Errorf("wrong opposite of black")
	}
}
