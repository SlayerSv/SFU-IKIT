package pr4

import (
	"fmt"
	"testing"
)

var tests = []struct {
	from string
	to   string
}{
	{"d4", "d5"},
	{"d5", "d4"},
	{"d4", "c5"},
	{"c5", "d4"},
	{"d4", "c4"},
	{"c4", "d4"},
	{"d4", "c3"},
	{"c3", "d4"},
}

func TestKingValidMove(t *testing.T) {
	var board = NewChessBoard()
	for _, tt := range tests {
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		pTo, err := NewChessBoardPosition(tt.to)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		king := NewChessPiece(KING, WHITE, board.GetField(pFrom))
		cm, err := king.GoToPosition(tt.to, board)
		if err != nil {
			t.Errorf("unexpected error: %v", err)
		}
		t.Run(cm.String(), func(t *testing.T) {
			if board.GetField(pFrom).GetChessPiece() != nil {
				t.Errorf("king didnt move from field %s", tt.from)
			}
			if board.GetField(pTo).GetChessPiece() != king {
				t.Errorf("king didnt move to field %s", tt.to)
			}
			if king.GetChessField() != board.GetField(pTo) {
				t.Errorf("king's chess field is incorrect: got %s, want %s",
					king.GetChessField().GetPosition().String(),
					board.GetField(pTo).GetPosition().String())
			}
		})
	}
}

func TestKingValidTake(t *testing.T) {
	var board = NewChessBoard()
	for _, tt := range tests {
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		pTo, err := NewChessBoardPosition(tt.to)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		king := NewChessPiece(KING, WHITE, board.GetField(pFrom))
		NewChessPiece(ROOK, BLACK, board.GetField(pTo))
		cm, err := king.GoToPosition(tt.to, board)
		if err != nil {
			t.Errorf("unexpected error: %v", err)
		}
		t.Run(cm.String(), func(t *testing.T) {
			if board.GetField(pFrom).GetChessPiece() != nil {
				t.Errorf("king didnt move from field %s", tt.from)
			}
			if board.GetField(pTo).GetChessPiece() != king {
				t.Errorf("king didnt move to field %s", tt.to)
			}
			if king.GetChessField() != board.GetField(pTo) {
				t.Errorf("king's chess field is incorrect: got %s, want %s",
					king.GetChessField().GetPosition().String(),
					board.GetField(pTo).GetPosition().String())
			}
		})
	}
}

func TestKingInvalidTake(t *testing.T) {
	var board = NewChessBoard()
	for _, tt := range tests {
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		pTo, err := NewChessBoardPosition(tt.to)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		king := NewChessPiece(KING, WHITE, board.GetField(pFrom))
		rook := NewChessPiece(ROOK, WHITE, board.GetField(pTo))
		_, err = king.GoToPosition(tt.to, board)
		if err == nil {
			t.Errorf("expected an error")
		}
		t.Run("K"+tt.to+tt.from, func(t *testing.T) {
			if board.GetField(pFrom).GetChessPiece() != king {
				t.Errorf("king didnt stay on field %s", tt.from)
			}
			if board.GetField(pTo).GetChessPiece() != rook {
				t.Errorf("allied piece didnt stay on field %s", tt.to)
			}
			if king.GetChessField() != board.GetField(pFrom) {
				t.Errorf("king's chess field is incorrect: got %s, want %s",
					king.GetChessField().GetPosition().String(),
					board.GetField(pFrom).GetPosition().String())
			}
			if rook.GetChessField() != board.GetField(pTo) {
				t.Errorf("allied's piece chess field is incorrect: got %s, want %s",
					rook.GetChessField().GetPosition().String(),
					board.GetField(pTo).GetPosition().String())
			}
		})
	}
}

func TestKingInvalidPosition(t *testing.T) {
	var tests = []struct {
		from string
		to   string
	}{
		{"d4", "d9"},
		{"d5", "04"},
		{"d4", "j1"},
		{"c5", "c4d"},
		{"c4", "c4"},
		{"c5", ""},
		{"c5", "a5"},
		{"c5", "c7"},
	}
	var board = NewChessBoard()
	for _, tt := range tests {
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		king := NewChessPiece(KING, WHITE, board.GetField(pFrom))
		t.Run("K"+tt.to+tt.from, func(t *testing.T) {
			_, err = king.GoToPosition(tt.to, board)
			if err == nil {
				t.Errorf("expected an error")
			}
			if board.GetField(pFrom).GetChessPiece() != king {
				t.Errorf("king didnt stay on field %s", tt.from)
			}
			if king.GetChessField() != board.GetField(pFrom) {
				t.Errorf("king's chess field is incorrect: got %s, want %s",
					king.GetChessField().GetPosition().String(),
					board.GetField(pFrom).GetPosition().String())
			}
		})
	}
}

func TestKingMoveOnCheckedField(t *testing.T) {
	var tests = []struct {
		from            string
		to              string
		side            side
		attackedByWhite bool
		attackedByBlack bool
		wantErr         bool
	}{
		{"d4", "d5", WHITE, true, false, false},
		{"d5", "d4", WHITE, true, true, true},
		{"d4", "c5", WHITE, false, true, true},
		{"c5", "d4", WHITE, false, false, false},
		{"d4", "c4", BLACK, true, false, true},
		{"c4", "d4", BLACK, true, true, true},
		{"d4", "c3", BLACK, false, true, false},
		{"c3", "d4", BLACK, false, false, false},
	}
	for _, tt := range tests {
		var board = NewChessBoard()
		pFrom, err := NewChessBoardPosition(tt.from)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		pTo, err := NewChessBoardPosition(tt.to)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		field := board.GetField(pTo)
		if tt.attackedByWhite {
			field.SetAttackedBy(WHITE)
		}
		if tt.attackedByBlack {
			field.SetAttackedBy(BLACK)
		}
		king := NewChessPiece(KING, tt.side, board.GetField(pFrom))
		t.Run(fmt.Sprintf("%s %s K: %v AttackW: %v AttackB: %v", tt.from, tt.to,
			tt.side, tt.attackedByWhite, tt.attackedByBlack), func(t *testing.T) {
			_, err = king.GoToPosition(tt.to, board)
			if err == nil && tt.wantErr {
				t.Errorf("expected an error")
			}
			if err != nil && !tt.wantErr {
				t.Errorf("unexpected error %v w: %v b: %v", err,
					field.IsAttackedBy(WHITE), field.IsAttackedBy(BLACK))
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
			oppositeSide := WHITE
			if oppositeSide == king.GetSide() {
				oppositeSide = BLACK
			}
			rook.GetChessField().SetAttackedBy(oppositeSide)
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
			if err == nil && tt.wantErr {
				t.Errorf("expected an error")
			}
			if err != nil && !tt.wantErr {
				t.Errorf("unexpected error %v", err)
			}
			if tt.wantErr {
				return
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
