package adt

import scala.reflect.ClassTag

class ArrayQueue[A: ClassTag] extends Queue[A] {
    private var data = Array.fill(10)(null.asInstanceOf[A])
    private var front = 0
    private var back = 0

    def enqueue(a: A): Unit = {
        if ((back + 1) % data.length == front) {
            val tmp = Array.fill(data.length * 2)(null.asInstanceOf[A])
            for (i <- 0 until data.length - 1) {
                tmp(i) = data((front + i) % data.length)
            }
            front = 0
            back = data.length - 1
            data = tmp
        }
        data(back) = a
        back = (back + 1) % data.length
    }

    def dequeue(): A = {
        val ret = data(front)
        front = (front + 1) % data.length
        ret
    }

    def peek: A = data(front)

    def isEmpty: Boolean = front == back
}