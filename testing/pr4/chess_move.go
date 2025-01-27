package pr4

type chessMove struct {
	oldPos        chessBoardPosition
	newPos        chessBoardPosition
	pieceType     chessPieceType
	isTake        bool
	isShortCastle bool
	isLongCastle  bool
}
