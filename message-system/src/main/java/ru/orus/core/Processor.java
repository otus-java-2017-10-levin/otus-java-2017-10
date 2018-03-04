package ru.orus.core;

interface Processor<T, R> extends Subscriber<T>, Publisher<R> {

}
