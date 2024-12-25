package main

import (
	"bufio"
	"os"
	"strconv"
)

func writeFiles() {
	usersFile, err := os.OpenFile("C:\\Users\\Public\\documents\\users.csv",
		os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening users file")
	}
	friendsFile, err := os.OpenFile("C:\\Users\\Public\\documents\\friends.csv",
		os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening friends file")
	}
	threadsFile, err := os.OpenFile("C:\\Users\\Public\\documents\\threads.csv",
		os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening threads file")
	}
	postsFile, err := os.OpenFile("C:\\Users\\Public\\documents\\posts.csv",
		os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening posts file")
	}
	likesFile, err := os.OpenFile("C:\\Users\\Public\\documents\\likes.csv",
		os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening likes file")
	}

	writer := bufio.NewWriter(usersFile)
	for i, user := range users {
		writer.WriteString(strconv.Itoa(i + 1))
		writer.WriteByte(',')
		writer.WriteString(user.name)
		writer.WriteByte(',')
		writer.WriteString(user.created_at.Format("02-01-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()

	writer = bufio.NewWriter(friendsFile)
	for _, friend := range friends {
		writer.WriteString(strconv.Itoa(friend.user1ID + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(friend.user2ID + 1))
		writer.WriteByte(',')
		writer.WriteString(friend.created_at.Format("02-01-2006 15:04:05"))
		writer.WriteByte('\n')
		writer.WriteString(strconv.Itoa(friend.user2ID + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(friend.user1ID + 1))
		writer.WriteByte(',')
		writer.WriteString(friend.created_at.Format("02-01-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()

	writer = bufio.NewWriter(threadsFile)
	for i, thread := range threads {
		writer.WriteString(strconv.Itoa(i + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(thread.userID + 1))
		writer.WriteByte(',')
		writer.WriteString(thread.title)
		writer.WriteByte(',')
		writer.WriteString(thread.created_at.Format("02-01-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()

	writer = bufio.NewWriter(postsFile)
	for i, post := range posts {
		writer.WriteString(strconv.Itoa(i + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(post.userID + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(post.threadID + 1))
		writer.WriteByte(',')
		writer.WriteString(post.body)
		writer.WriteByte(',')
		writer.WriteString(post.created_at.Format("02-01-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()

	writer = bufio.NewWriter(likesFile)
	for _, like := range likes {
		writer.WriteString(strconv.Itoa(like.postID + 1))
		writer.WriteByte(',')
		writer.WriteString(strconv.Itoa(like.userID + 1))
		writer.WriteByte(',')
		writer.WriteString(like.created_at.Format("02-01-2006 15:04:05"))
		writer.WriteByte('\n')
	}
	writer.Flush()
}
