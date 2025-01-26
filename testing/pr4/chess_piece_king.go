package pr4

import (
	"strings"
)

type chessPieceKing struct {
	chessPiece
	moved bool
}

func (k *chessPieceKing) GetPosition() *chessField {
	return k.chessField
}

func (k *chessPieceKing) GetSide() side {
	return k.side
}

func (k *chessPieceKing) GoToPosition(newPosition string, board *chessBoard) (chessMove, error) {
	// check for castling move
	if !k.moved {
		if k.side == WHITE && (strings.ToLower(newPosition) == "f1" ||
			strings.ToLower(newPosition) == "b1") {
			return k.Castle(newPosition, board)
		} else if k.side == BLACK && (strings.ToLower(newPosition) == "f8" ||
			strings.ToLower(newPosition) == "b8") {
			return k.Castle(newPosition, board)
		}
	}
	var cm chessMove

	// check new position for validity and get coordinates of new field
	newCoords, err := NewChessBoardCoordinates(newPosition)
	if err != nil {
		return cm, err
	}
	oldCoords := k.chessField.chessBoardCoordinates

	// check if old and new chess fields are adjacent (king moves only 1 square)
	rowDiff := oldCoords.row - newCoords.row
	if rowDiff < 1 || rowDiff > 1 {
		return cm, errIllegalMove
	}
	colDiff := oldCoords.col - newCoords.col
	if colDiff < 1 || colDiff > 1 {
		return cm, errIllegalMove
	}

	// check if field is occupied by same side piece
	newField := board.GetField(newCoords)
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
	cm = chessMove{
		oldCoords: oldCoords,
		newCoords: newCoords,
		isTake:    isTake,
	}
	return cm, nil
}

func (k *chessPieceKing) Castle(newPosition string, board *chessBoard) (chessMove, error) {
	var cm chessMove

	return cm, nil
}
