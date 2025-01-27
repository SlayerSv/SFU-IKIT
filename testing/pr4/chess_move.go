package pr4

type chessMove struct {
	oldPos        chessBoardPosition
	newPos        chessBoardPosition
	isTake        bool
	isShortCastle bool
	isLongCastle  bool
}
