package pr4

type chessPieceRook struct {
	chessPiece
	moved bool
}

func (r *chessPieceRook) GetChessField() *chessField {
	return r.chessField
}

func (r *chessPieceRook) SetChessField(cf *chessField) {
	r.chessField = cf
}

func (r *chessPieceRook) GetSide() side {
	return r.side
}

func (r *chessPieceRook) GetType() chessPieceType {
	return r.pieceType
}

func (r *chessPieceRook) HasMoved() bool {
	return r.moved
}

func (r *chessPieceRook) SetMoved() {
	r.moved = true
}

func (r *chessPieceRook) GoToPosition(newPosition string, board *chessBoard) (chessMove, error) {
	var cm chessMove
	toPos, err := NewChessBoardPosition(newPosition)
	if err != nil {
		return cm, err
	}
	fromPos := r.GetChessField().GetPosition()

	// check for same position
	if toPos.GetCol() == fromPos.GetCol() && toPos.GetRow() == fromPos.GetRow() {
		return cm, errIllegalMove
	}

	// check move is a straight vertical or horizontal line
	if toPos.GetCol() != fromPos.GetCol() && toPos.GetRow() != fromPos.GetRow() {
		return cm, errIllegalMove
	}

	// check for any piece blocking path between positions
	movePath := board.collectFields(fromPos, toPos)
	for i, field := range movePath {
		if i != 0 && i != len(movePath)-1 && field.GetChessPiece() != nil {
			return cm, errIllegalMove
		}
	}

	// check if destination field piece is not of the same side
	isTake := false
	lastFieldPiece := movePath[len(movePath)-1].GetChessPiece()
	if lastFieldPiece != nil {
		if lastFieldPiece.GetSide() == r.GetSide() {
			return cm, errIllegalMove
		}
		isTake = true
	}

	r.SetMoved()
	r.GetChessField().SetChessPiece(nil)
	r.SetChessField(board.GetField(toPos))
	r.GetChessField().SetChessPiece(r)

	cm = NewChessMove(fromPos, toPos, r.GetType(), isTake, false, false)

	return cm, nil
}
