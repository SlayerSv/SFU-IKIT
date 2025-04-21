package server

import (
	"fmt"
	"log"
	"os"
)

func NewFileLogger(logFilePath string) (*log.Logger, error) {
	file, err := os.OpenFile(logFilePath, os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		return nil, fmt.Errorf("open log file: %w", err)
	}
	return log.New(file, "", log.Flags()), nil
}
