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
	newPos, err := NewChessBoardPosition(newPosition)
	if err != nil {
		return cm, err
	}
	oldPos := r.GetChessField().GetPosition()

	// check for same position
	if newPos.GetCol() == oldPos.GetCol() && newPos.GetRow() == oldPos.GetRow() {
		return cm, errIllegalMove
	}

	// check move is a straight vertical or horizontal line
	if newPos.GetCol() != oldPos.GetCol() && newPos.GetRow() != oldPos.GetRow() {
		return cm, errIllegalMove
	}

	// check for any piece blocking path between positions
	movePath := board.collectFields(oldPos, newPos)
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
	r.SetChessField(board.GetField(newPos))
	r.GetChessField().SetChessPiece(r)

	cm = chessMove{
		oldPos:    oldPos,
		newPos:    newPos,
		pieceType: r.GetType(),
		isTake:    isTake,
	}

	return cm, nil
}
