package pr4

import (
	"errors"
)

var errInvalidChessPosition error = errors.New("invalid chess position")
var errIllegalMove error = errors.New("illegal move")

type chessBoard [8][8]chessField

func (cb *chessBoard) GetField(cbp chessBoardPosition) *chessField {
	return &cb[cbp.row-1][cbp.col-'a']
}

// from and to must form a straight line in any direction including diagonal
func (cb *chessBoard) collectFields(from, to chessBoardPosition) []*chessField {
	var rowDir int8
	if to.GetRow() < from.GetRow() {
		rowDir = -1
	} else if to.GetRow() > from.GetRow() {
		rowDir = 1
	}
	var colDir int8
	if to.GetCol() < from.GetCol() {
		colDir = -1
	} else if to.GetCol() > from.GetCol() {
		rowDir = 1
	}

	fields := make([]*chessField, 0, 8)
	for from.GetRow() != to.GetRow() || from.GetCol() != to.GetCol() {
		fields = append(fields, cb.GetField(from))
		if colDir != 0 {
			err := from.SetCol(byte(int8(from.GetCol()) + colDir))
			if err != nil {
				return fields
			}
		}
		if rowDir != 0 {
			err := from.SetRow(from.GetRow() + rowDir)
			if err != nil {
				return fields
			}
		}
	}
	// last field is not added in the loop
	fields = append(fields, cb.GetField(from))
	return fields
}

type chessField struct {
	chessPiece IChessPiece
	chessBoardPosition
	attackedByWhite bool
	attackedByBlack bool
}

func (cf chessField) IsAttackedBy(side side) bool {
	if side == WHITE {
		return cf.attackedByWhite
	}
	return cf.attackedByBlack
}

func (cf chessField) GetChessPiece() IChessPiece {
	return cf.chessPiece
}

func (cf *chessField) SetChessPiece(chessPiece IChessPiece) {
	cf.chessPiece = chessPiece
}

func (cf *chessField) SetAttackedBy(side side) {
	if side == WHITE {
		cf.attackedByWhite = true
		return
	}
	cf.attackedByBlack = true
}

type chessBoardPosition struct {
	col byte
	row int8
}

func (cbp chessBoardPosition) GetRow() int8 {
	return cbp.row
}

func (cbp *chessBoardPosition) SetRow(row int8) error {
	if row < 1 || row > 8 {
		return errInvalidChessPosition
	}
	cbp.row = row
	return nil
}

func (cbp chessBoardPosition) GetCol() byte {
	return cbp.col
}

func (cbp *chessBoardPosition) SetCol(col byte) error {
	if col < 'a' || col > 'h' {
		return errInvalidChessPosition
	}
	cbp.col = col
	return nil
}

func (cbp chessBoardPosition) GetPosition() chessBoardPosition {
	return cbp
}

func NewChessBoardPosition(position string) (chessBoardPosition, error) {
	cbp := chessBoardPosition{}
	if len(position) != 2 {
		return cbp, errInvalidChessPosition
	}
	err := cbp.SetCol(position[0])
	if err != nil {
		return cbp, errInvalidChessPosition
	}
	row := int8(position[1]) - '0'
	err = cbp.SetRow(row)
	if err != nil {
		return chessBoardPosition{}, errInvalidChessPosition
	}
	return cbp, nil
}
