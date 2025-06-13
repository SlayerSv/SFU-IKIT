package kafka

import (
	"context"

	"github.com/SlayerSv/SFU-IKIT/integration/pr3/broker"

	"github.com/segmentio/kafka-go"
)

type Writer struct {
	*kafka.Writer
}

func NewWriter(address, topic string) *Writer {
	writer := &kafka.Writer{
		Addr:     kafka.TCP(address),
		Topic:    topic,
		Balancer: &kafka.LeastBytes{},
	}
	return &Writer{writer}
}

func (k *Writer) Send(msg broker.Message) error {
	return k.WriteMessages(context.Background(), kafka.Message{
		Value: []byte(msg.Value),
		Time:  msg.Time,
	})
}

func (k *Writer) GetTopic() string {
	return k.Topic
}

type Reader struct {
	*kafka.Reader
}

func NewReader(address, topic string) *Reader {
	reader := kafka.NewReader(kafka.ReaderConfig{
		Brokers: []string{address},
		Topic:   topic,
	})
	return &Reader{reader}
}

func (k *Reader) Read() (broker.Message, error) {
	msg, err := k.ReadMessage(context.Background())
	if err != nil {
		return broker.Message{}, err
	}
	return broker.Message{
		Value: string(msg.Value),
		Time:  msg.Time,
	}, nil
}

func (k *Reader) GetTopic() string {
	return k.Config().Topic
}
