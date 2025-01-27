package pr4

type IChessPiece interface {
	GetChessField() *chessField
	SetChessField(*chessField)
	GoToPosition(string, *chessBoard) (chessMove, error)
	GetSide() side
	GetType() chessPieceType
}

type chessPiece struct {
	pieceType  chessPieceType
	side       side
	chessField *chessField
}

func NewChessPiece(cpt chessPieceType, side side, cf *chessField) IChessPiece {
	switch cpt {
	case ROOK:
		return &chessPieceRook{
			chessPiece: chessPiece{
				pieceType:  cpt,
				side:       side,
				chessField: cf,
			},
			moved: false,
		}
	case KING:
		return &chessPieceKing{
			chessPiece: chessPiece{
				pieceType:  cpt,
				side:       side,
				chessField: cf,
			},
			moved: false,
		}
	default:
		return nil
	}
}

type IChessPieceCastler interface {
	IChessPiece
	HasMoved() bool
	SetMoved()
}

type chessPieceType int8

const (
	PAWN chessPieceType = iota
	KNIGHT
	BISHOP
	ROOK
	QUEEN
	KING
)

type side int8

const (
	WHITE side = iota
	BLACK
)
