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
		rook := &chessPieceRook{
			chessPiece: chessPiece{
				pieceType:  cpt,
				side:       side,
				chessField: cf,
			},
			moved: false,
		}
		cf.SetChessPiece(rook)
		return rook
	case KING:
		king := &chessPieceKing{
			chessPiece: chessPiece{
				pieceType:  cpt,
				side:       side,
				chessField: cf,
			},
			moved: false,
		}
		cf.SetChessPiece(king)
		return king
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

func (pt chessPieceType) String() string {
	switch pt {
	case KNIGHT:
		return "N"
	case BISHOP:
		return "B"
	case ROOK:
		return "R"
	case QUEEN:
		return "Q"
	case KING:
		return "K"
	default:
		return ""
	}
}

type side int8

const (
	WHITE side = iota
	BLACK
)
