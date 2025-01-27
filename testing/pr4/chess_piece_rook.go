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
	return chessMove{}, nil
}
