package pr4

import "strings"

type chessMove struct {
	from      chessBoardPosition
	to        chessBoardPosition
	pieceType chessPieceType
	isTake    bool
	isCheck   bool
	isMate    bool
}

func NewChessMove(from, to chessBoardPosition, cpt chessPieceType, isTake, isCheck, isMate bool) chessMove {
	return chessMove{
		from:      from,
		to:        to,
		pieceType: cpt,
		isTake:    isTake,
		isCheck:   isCheck,
		isMate:    isMate,
	}
}

// returns a long algebraic notation of the move to eliminate ambiguity
func (cm chessMove) String() string {
	var b strings.Builder
	b.WriteString(cm.pieceType.String())
	b.WriteString(cm.from.String())
	if cm.isTake {
		b.WriteByte('x')
	}
	b.WriteString(cm.to.String())
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
