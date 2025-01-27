package pr4

import (
	"strings"
)

type chessPieceKing struct {
	chessPiece
	moved bool
}

func (k *chessPieceKing) GetChessField() *chessField {
	return k.chessField
}

func (k *chessPieceKing) GetSide() side {
	return k.side
}

func (k *chessPieceKing) GetType() chessPieceType {
	return k.pieceType
}

func (k *chessPieceKing) HasMoved() bool {
	return k.moved
}

func (k *chessPieceKing) SetMoved() {
	k.moved = true
}

func (k *chessPieceKing) GoToPosition(newPosition string, board *chessBoard) (chessMove, error) {
	// check for castling move
	if !k.moved {
		if k.side == WHITE && (strings.ToLower(newPosition) == "f1" ||
			strings.ToLower(newPosition) == "h1" ||
			strings.ToLower(newPosition) == "b1" ||
			strings.ToLower(newPosition) == "a1") {
			return k.Castle(newPosition, board)
		} else if k.side == BLACK && (strings.ToLower(newPosition) == "f8" ||
			strings.ToLower(newPosition) == "h8" ||
			strings.ToLower(newPosition) == "a8" ||
			strings.ToLower(newPosition) == "b8") {
			return k.Castle(newPosition, board)
		}
	}
	var cm chessMove

	// check new position for validity and get Position of new field
	newPos, err := NewChessBoardPosition(newPosition)
	if err != nil {
		return cm, err
	}
	oldPos := k.chessField.chessBoardPosition

	// check if old and new chess fields are adjacent (king moves only 1 square)
	rowDiff := oldPos.row - newPos.row
	if rowDiff < 1 || rowDiff > 1 {
		return cm, errIllegalMove
	}
	colDiff := oldPos.col - newPos.col
	if colDiff < 1 || colDiff > 1 {
		return cm, errIllegalMove
	}

	// check if field is occupied by same side piece
	newField := board.GetField(newPos)
	piece := newField.chessPiece
	if piece != nil && piece.GetSide() == k.side {
		return cm, errIllegalMove
	}

	isTake := false
	if piece != nil && piece.GetSide() != k.side {
		isTake = true
	}
	// check if new field is under attack by the opposite side
	otherSide := WHITE
	if k.side == otherSide {
		otherSide = BLACK
	}
	if newField.IsAttackedBy(otherSide) {
		return cm, errIllegalMove
	}

	// make move - set new values for fields and chess piece
	k.chessField.chessPiece = nil
	newField.chessPiece = k
	k.chessField = newField
	cm = chessMove{
		oldPos: oldPos,
		newPos: newPos,
		isTake: isTake,
	}
	return cm, nil
}

func (k *chessPieceKing) Castle(newPosition string, board *chessBoard) (chessMove, error) {
	var cm chessMove
	newKingPos, err := NewChessBoardPosition(newPosition)
	if err != nil {
		return cm, err
	}
	// correct position in case king was moved to rook
	if newKingPos.GetCol() == 'a' {
		newKingPos.SetCol('b')
	}
	if newKingPos.GetCol() == 'h' {
		newKingPos.SetCol('f')
	}
	oldKingPos := k.GetChessField().GetPosition()

	// determine presumable rook position
	oldRookPos := oldKingPos
	if newKingPos.col < oldKingPos.col {
		oldRookPos.SetCol('a')
	} else {
		oldRookPos.SetCol('h')
	}

	// check that rook is in its place, is of same side and hasn't moved
	sidePiece := board.GetField(oldRookPos).GetChessPiece()
	if sidePiece == nil {
		return cm, errIllegalMove
	}
	if sidePiece.GetType() != ROOK || sidePiece.GetSide() != k.GetSide() {
		return cm, errIllegalMove
	}
	rook := sidePiece.(*chessPieceRook)
	if rook.HasMoved() {
		return cm, errIllegalMove
	}

	// check chess fields are not under attack and are free
	oppositeSide := WHITE
	if k.GetSide() == oppositeSide {
		oppositeSide = BLACK
	}
	castlingPath := board.collectFields(oldKingPos, oldRookPos)
	for i, field := range castlingPath {
		// check fields are free (except for king and rook fields)
		if i != 0 && i != (len(castlingPath)-1) && field.GetChessPiece() != nil {
			return cm, errIllegalMove
		}
		// check fields are not under attack
		if field.IsAttackedBy(oppositeSide) {
			return cm, errIllegalMove
		}
	}

	// make moves
	// move king
	newKingField := board.GetField(newKingPos)
	k.chessField.chessPiece = nil
	newKingField.SetChessPiece(k)
	k.chessField = newKingField

	// move rook
	newRookPos := k.GetChessField().GetPosition()
	if oldRookPos.GetCol() < oldKingPos.GetCol() {
		newRookPos.SetCol(newKingPos.GetCol() + 1)
	} else {
		newRookPos.SetCol(newKingPos.GetCol() - 1)
	}
	newRookField := board.GetField(newRookPos)
	rook.GetChessField().SetChessPiece(nil)
	newRookField.SetChessPiece(rook)
	rook.SetChessField(newRookField)

	cm = chessMove{
		oldPos: oldKingPos,
		newPos: newKingPos,
	}
	if oldRookPos.col < oldKingPos.col {
		cm.isShortCastle = true
	} else {
		cm.isLongCastle = true
	}
	return cm, nil
}
