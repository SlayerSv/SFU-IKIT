package pr4

import (
	"errors"
)

var errInvalidChessField error = errors.New("invalid chess field")
var errIllegalMove error = errors.New("illegal move")

type chessBoard [8][8]chessField

func (cb *chessBoard) GetField(cbp chessBoardCoordinates) *chessField {
	return &cb[int8(cbp.row-'a')][cbp.col-1]
}

type chessField struct {
	chessPiece IChessPiece
	chessBoardCoordinates
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

type chessBoardCoordinates struct {
	row byte
	col int8
}

func (cbp chessBoardCoordinates) GetRow() byte {
	return cbp.row
}

func (cbp *chessBoardCoordinates) SetRow(row byte) error {
	if row < 'a' || row > 'h' {
		return errInvalidChessField
	}
	cbp.row = row
	return nil
}

func (cbp *chessBoardCoordinates) SetCol(col int8) error {
	if col < 1 || col > 8 {
		return errInvalidChessField
	}
	cbp.col = col
	return nil
}

func NewChessBoardCoordinates(position string) (chessBoardCoordinates, error) {
	cbp := chessBoardCoordinates{}
	if len(position) != 2 {
		return cbp, errInvalidChessField
	}
	err := cbp.SetRow(position[0])
	if err != nil {
		return cbp, errInvalidChessField
	}
	col := int8(position[1] - '0')
	err = cbp.SetCol(col)
	if err != nil {
		return chessBoardCoordinates{}, errInvalidChessField
	}
	return cbp, nil
}
