package main

import (
	"bufio"
	"os"
	"strconv"
)

func writeFiles() {

	usersFile, err := os.OpenFile("users.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening users file")
	}
	friendsFile, err := os.OpenFile("friends.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening friends file")
	}
	threadsFile, err := os.OpenFile("threads.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening threads file")
	}
	postsFile, err := os.OpenFile("posts.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening posts file")
	}
	likesFile, err := os.OpenFile("likes.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening likes file")
	}

	writer := bufio.NewWriter(usersFile)
	for i, user := range users {
		writer.WriteString(strconv.Itoa(i + 1))
		writer.WriteByte(',')
		writer.WriteString(user.name)
		writer.WriteByte(',')
		writer.WriteString(user.created_at.Format("01-02-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()

	writer = bufio.NewWriter(friendsFile)
	for i, friend := range friends {
		writer.WriteString(strconv.Itoa(i + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(friend.user1ID))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(friend.user2ID))
		writer.WriteByte(',')
		writer.WriteString(friend.created_at.Format("01-02-2006 15:04:05"))
		writer.WriteByte('\n')
		writer.WriteString(strconv.Itoa(i + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(friend.user2ID))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(friend.user1ID))
		writer.WriteByte(',')
		writer.WriteString(friend.created_at.Format("01-02-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()

	writer = bufio.NewWriter(threadsFile)
	for i, thread := range threads {
		writer.WriteString(strconv.Itoa(i + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(thread.userID))
		writer.WriteByte(',')
		writer.WriteString(thread.created_at.Format("01-02-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()

	writer = bufio.NewWriter(postsFile)
	for i, post := range posts {
		writer.WriteString(strconv.Itoa(i + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(post.userID))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(post.threadID))
		writer.WriteByte(',')
		writer.WriteString(post.created_at.Format("01-02-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()

	writer = bufio.NewWriter(likesFile)
	for i, like := range likes {
		writer.WriteString(strconv.Itoa(i + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(like.postID))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(like.userID))
		writer.WriteByte(',')
		writer.WriteString(like.created_at.Format("01-02-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()
}
