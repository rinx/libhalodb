package main

// #cgo CFLAGS: -I${SRCDIR}/../../target/native
// #cgo LDFLAGS: -L${SRCDIR}/../../target/native -lhalodb
//
// #include <stdlib.h>
// #include <libhalodb.h>
import "C"
import (
	"fmt"
	"unsafe"
)

type haloDB struct {
	isolate *C.graal_isolate_t
	thread  *C.graal_isolatethread_t
}

type HaloDB interface {
	Open(path string)
	Put(key, value string)
	Get(key string) string
	Delete(key string)
	Size() uint64
	Close()
}

func New() (HaloDB, error) {
	var isolate *C.graal_isolate_t
	var thread *C.graal_isolatethread_t

	if C.graal_create_isolate(nil, &isolate, &thread) != 0 {
		return nil, fmt.Errorf("failed to initialize")
	}

	return &haloDB{
		isolate: isolate,
		thread:  thread,
	}, nil
}

func (h *haloDB) Open(path string) {
	cspath := C.CString(path)
	defer C.free(unsafe.Pointer(cspath))
	C.halodb_open(*(*C.longlong)(unsafe.Pointer(&h.thread)), cspath)
}

func (h *haloDB) Put(key, value string) {
	csKey := C.CString(key)
	defer C.free(unsafe.Pointer(csKey))
	csValue := C.CString(value)
	defer C.free(unsafe.Pointer(csValue))
	C.halodb_put(*(*C.longlong)(unsafe.Pointer(&h.thread)), csKey, csValue)
}

func (h *haloDB) Get(key string) string {
	csKey := C.CString(key)
	defer C.free(unsafe.Pointer(csKey))
	return C.GoString(C.halodb_get(*(*C.longlong)(unsafe.Pointer(&h.thread)), csKey))
}

func (h *haloDB) Delete(key string) {
	csKey := C.CString(key)
	defer C.free(unsafe.Pointer(csKey))
	C.halodb_delete(*(*C.longlong)(unsafe.Pointer(&h.thread)), csKey)
}

func (h *haloDB) Size() uint64 {
	res := C.halodb_size(*(*C.longlong)(unsafe.Pointer(&h.thread)))
	return *(*uint64)(unsafe.Pointer(&res))
}

func (h *haloDB) Close() {
	C.halodb_close(*(*C.longlong)(unsafe.Pointer(&h.thread)))
}

func main() {
	haloDB, err := New()
	if err != nil {
		println(err)
		return
	}

	haloDB.Open(".halodb")

	println(haloDB.Size())

	haloDB.Put("k1", "this is written from Go!")
	haloDB.Put("k2", "this is written from Go! 2")

	println(haloDB.Size())

	println(haloDB.Get("k1"))
	println(haloDB.Get("k2"))

	println(haloDB.Size())

	haloDB.Delete("k1")

	println(haloDB.Size())
}
