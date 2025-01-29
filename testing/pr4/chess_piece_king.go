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

func (k *chessPieceKing) SetChessField(cf *chessField) {
	k.chessField = cf
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

func (k *chessPieceKing) GoToPosition(to string, board *chessBoard) (chessMove, error) {
	// check for castling move
	if !k.HasMoved() {
		toLower := strings.ToLower(to)
		if k.side == WHITE && (toLower == "f1" ||
			toLower == "b1") {
			return k.Castle(toLower, board)
		} else if k.side == BLACK && (toLower == "f8" ||
			toLower == "b8") {
			return k.Castle(toLower, board)
		}
	}
	var cm chessMove

	// check to position for validity and get position of to field
	toPos, err := NewChessBoardPosition(to)
	if err != nil {
		return cm, err
	}

	// check to position is not the same as old one
	fromPos := k.GetChessField().GetPosition()
	if toPos.GetCol() == fromPos.GetCol() && toPos.GetRow() == fromPos.GetRow() {
		return cm, errIllegalMove
	}

	// check if from and to chess fields are adjacent (king moves only 1 square)
	rowDiff := fromPos.GetRow() - toPos.GetRow()
	if rowDiff < -1 || rowDiff > 1 {
		return cm, errIllegalMove
	}
	colDiff := int8(fromPos.GetCol()) - int8(toPos.GetCol())
	if colDiff < -1 || colDiff > 1 {
		return cm, errIllegalMove
	}

	// check if field is occupied by same side piece
	kingToField := board.GetField(toPos)
	piece := kingToField.GetChessPiece()

	isTake := false
	if piece != nil {
		if piece.GetSide() == k.GetSide() {
			return cm, errIllegalMove
		}
		isTake = true
	}

	// check if new field is under attack by the opposite side
	if kingToField.IsAttackedBy(k.GetSide().GetOpposite()) {
		return cm, errIllegalMove
	}

	// make move - set to values for fields and chess piece
	k.GetChessField().SetChessPiece(nil)
	kingToField.SetChessPiece(k)
	k.SetChessField(kingToField)
	k.SetMoved()
	cm = NewChessMove(fromPos, toPos, k.GetType(), isTake, false, false)
	return cm, nil
}

func (k *chessPieceKing) Castle(to string, board *chessBoard) (chessMove, error) {
	var cm chessMove
	kingToPos, _ := NewChessBoardPosition(to)
	kingFromPos := k.GetChessField().GetPosition()

	// determine presumable rook position
	rookFromPos := kingFromPos
	if kingToPos.GetCol() < kingFromPos.GetCol() {
		rookFromPos.SetCol('a')
	} else {
		rookFromPos.SetCol('h')
	}

	// check that rook is in its place, is of same side and hasn't moved
	sidePiece := board.GetField(rookFromPos).GetChessPiece()
	if sidePiece == nil {
		return cm, errIllegalMove
	}
	if sidePiece.GetType() != ROOK || sidePiece.GetSide() != k.GetSide() {
		return cm, errIllegalMove
	}
	rook := sidePiece.(IChessPieceCastler)
	if rook.HasMoved() {
		return cm, errIllegalMove
	}

	castlingPath := board.collectFields(kingFromPos, rookFromPos)
	for i, field := range castlingPath {
		// check fields are free (except for king and rook fields)
		if i != 0 && i != (len(castlingPath)-1) && field.GetChessPiece() != nil {
			return cm, errIllegalMove
		}
		// check fields are not under attack
		if field.IsAttackedBy(k.GetSide().GetOpposite()) {
			return cm, errIllegalMove
		}
	}

	// make moves
	// move king
	newKingField := board.GetField(kingToPos)
	k.GetChessField().SetChessPiece(nil)
	newKingField.SetChessPiece(k)
	k.SetChessField(newKingField)
	k.SetMoved()

	// move rook
	// place rook to the opposite side of the king
	newRookPos := k.GetChessField().GetPosition()
	if rookFromPos.GetCol() < kingFromPos.GetCol() {
		newRookPos.SetCol(kingToPos.GetCol() + 1)
	} else {
		newRookPos.SetCol(kingToPos.GetCol() - 1)
	}
	newRookField := board.GetField(newRookPos)
	rook.GetChessField().SetChessPiece(nil)
	newRookField.SetChessPiece(rook)
	rook.SetChessField(newRookField)
	rook.SetMoved()

	cm = NewChessMove(kingFromPos, kingToPos, k.GetType(), false, false, false)
	return cm, nil
}
