package pr4

import (
	"fmt"
	"testing"
)

func TestNewChessBoard(t *testing.T) {
	t.Run("validity of field positions", func(t *testing.T) {
		board := NewChessBoard()
		for i := 0; i < 8; i++ {
			for j := 0; j < 8; j++ {
				pos := string(byte(j)+'a') + string(byte(i)+'0'+1)
				cbp, err := NewChessBoardPosition(pos)
				if err != nil {
					t.Fatalf("unexpected error %v, pos: %s", err, pos)
				}
				field := board.GetField(cbp)
				if field == nil {
					t.Fatalf("null field %s at indices %d %d", pos, i, j)
				}
				if field.GetPosition().String() != pos {
					t.Errorf("wrong pos name got %s want %s", field.GetPosition().String(), pos)
				}
			}
		}
	})
}

func TestCollectFields(t *testing.T) {
	tests := []struct {
		from string
		to   string
		want []string
	}{
		{"a1", "a8", []string{"a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8"}},
		{"d3", "d5", []string{"d3", "d4", "d5"}},
		{"a1", "h1", []string{"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"}},
		{"a1", "h8", []string{"a1", "b2", "c3", "d4", "e5", "f6", "g7", "h8"}},
		{"h8", "a1", []string{"h8", "g7", "f6", "e5", "d4", "c3", "b2", "a1"}},
		{"e2", "e2", []string{"e2"}},
	}
	board := NewChessBoard()
	for _, tt := range tests {
		t.Run(fmt.Sprintf("%s-%s", tt.from, tt.to), func(t *testing.T) {
			pFrom, err := NewChessBoardPosition(tt.from)
			if err != nil {
				t.Fatalf("unexpected error %v, pos: %s", err, tt.from)
			}
			pTo, err := NewChessBoardPosition(tt.to)
			if err != nil {
				t.Fatalf("unexpected error %v, pos: %s", err, tt.to)
			}
			fields := board.collectFields(pFrom, pTo)
			if len(fields) != len(tt.want) {
				t.Errorf("wrong number of fields: got %d want %d", len(fields), len(tt.want))
			}
			for i, field := range fields {
				if field.GetPosition().String() != tt.want[i] {
					t.Errorf("wrong field: got %s want %s at index %d",
						field.GetPosition().String(), tt.want[i], i)
				}
			}
		})
	}
}

func TestChessBoardPosition(t *testing.T) {
	tests := []struct {
		pos     string
		wantErr bool
	}{
		{"a1", false},
		{"a9", true},
		{"j1", true},
		{"d31", true},
		{"", true},
		{"ab", true},
		{"13", true},
		{"d0", true},
		{"d3", false},
	}
	for _, tt := range tests {
		t.Run(tt.pos, func(t *testing.T) {
			pos, err := NewChessBoardPosition(tt.pos)
			if err != nil && !tt.wantErr {
				t.Errorf("unexpected error %v", err)
			}
			if err == nil && tt.wantErr {
				t.Errorf("expected an error")
			}
			if err == nil && !tt.wantErr && pos.String() != tt.pos {
				t.Errorf("wrong move: got %s want %s", pos.String(), tt.pos)
			}
		})
	}
}

func TestAttackedBy(t *testing.T) {
	pos, err := NewChessBoardPosition("a1")
	if err != nil {
		t.Fatalf("unexpected error %v", err)
	}
	tests := []struct {
		white bool
		black bool
	}{
		{false, false},
		{true, false},
		{false, true},
		{true, true},
	}
	for _, tt := range tests {
		t.Run(fmt.Sprintf("white: %v, black: %v", tt.white, tt.black), func(t *testing.T) {
			field := NewChessBoard().GetField(pos)
			if tt.white {
				field.SetAttackedBy(WHITE)
			}
			if tt.black {
				field.SetAttackedBy(BLACK)
			}
			if field.IsAttackedBy(WHITE) && !tt.white ||
				!field.IsAttackedBy(WHITE) && tt.white ||
				field.IsAttackedBy(BLACK) && !tt.black ||
				!field.IsAttackedBy(BLACK) && tt.black {
				t.Errorf("unexpected attacks: by white: %v want %v, by black: %v want %v",
					field.IsAttackedBy(WHITE), tt.white,
					field.IsAttackedBy(BLACK), tt.black)
			}
			field.ClearAttacked()
			if field.IsAttackedBy(WHITE) || field.IsAttackedBy(BLACK) {
				t.Errorf("unexpected attacks after clear: by white: %v, by black: %v",
					field.IsAttackedBy(WHITE), field.IsAttackedBy(BLACK))
			}
		})
	}
}
