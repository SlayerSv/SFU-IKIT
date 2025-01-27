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
		newPosLower := strings.ToLower(newPosition)
		// castling is allowed by moving king to the rook position
		// and to actual castling position
		if k.side == WHITE && (newPosLower == "f1" ||
			newPosLower == "h1" ||
			newPosLower == "b1" ||
			newPosLower == "a1") {
			return k.Castle(newPosLower, board)
		} else if k.side == BLACK && (newPosLower == "f8" ||
			newPosLower == "h8" ||
			newPosLower == "a8" ||
			newPosLower == "b8") {
			return k.Castle(newPosLower, board)
		}
	}
	var cm chessMove

	// check new position for validity and get Position of new field
	newPos, err := NewChessBoardPosition(newPosition)
	if err != nil {
		return cm, err
	}

	// check the new position is not the same as old one
	oldPos := k.chessField.chessBoardPosition
	if newPos.GetCol() == oldPos.GetCol() && newPos.GetRow() == oldPos.GetRow() {
		return cm, errIllegalMove
	}

	// check if old and new chess fields are adjacent (king moves only 1 square)
	rowDiff := oldPos.GetRow() - newPos.GetRow()
	if rowDiff < -1 || rowDiff > 1 {
		return cm, errIllegalMove
	}
	colDiff := int8(oldPos.GetCol()) - int8(newPos.GetCol())
	if colDiff < -1 || colDiff > 1 {
		return cm, errIllegalMove
	}

	// check if field is occupied by same side piece
	newKingField := board.GetField(newPos)
	piece := newKingField.GetChessPiece()
	if piece != nil && piece.GetSide() == k.side {
		return cm, errIllegalMove
	}

	isTake := false
	if piece != nil && piece.GetSide() != k.side {
		isTake = true
	}
	// check if new field is under attack by the opposite side
	oppositeSide := WHITE
	if k.side == oppositeSide {
		oppositeSide = BLACK
	}
	if newKingField.IsAttackedBy(oppositeSide) {
		return cm, errIllegalMove
	}

	// make move - set new values for fields and chess piece
	k.chessField.chessPiece = nil
	newKingField.SetChessPiece(k)
	k.chessField = newKingField
	k.SetMoved()
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
	if newKingPos.GetCol() < oldKingPos.GetCol() {
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
	k.SetMoved()

	// move rook
	// place rook to the opposite side of the king
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
	rook.SetMoved()

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
