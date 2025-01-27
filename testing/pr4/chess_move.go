package pr4

import "strings"

type chessMove struct {
	oldPos    chessBoardPosition
	newPos    chessBoardPosition
	pieceType chessPieceType
	isTake    bool
	isCheck   bool
	isMate    bool
}

// returns a long algebraic notation of the move to eliminate ambiguity
func (cm chessMove) String() string {
	var b strings.Builder
	b.WriteString(cm.pieceType.String())
	b.WriteString(cm.oldPos.String())
	if cm.isTake {
		b.WriteByte('x')
	}
	b.WriteString(cm.newPos.String())
	if cm.isCheck {
		b.WriteByte('+')
	}
	if cm.isMate {
		b.WriteByte('#')
	}
	move := b.String()
	if move == "Ke1g1" || move == "Ke8g8" {
		return "0-0"
	}
	if move == "Ke1c1" || move == "Ke8c8" {
		return "0-0-0"
	}
	return move
}
