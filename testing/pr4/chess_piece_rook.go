package pr4

type chessPieceRook struct {
	chessPiece
	moved bool
}

func (r *chessPieceRook) GetPosition() *chessField {
	return r.chessField
}

func (r *chessPieceRook) GetSide() side {
	return r.side
}

func (r *chessPieceRook) GoToPosition(newPosition string, board *chessBoard) (chessMove, error) {
	return chessMove{}, nil
}
