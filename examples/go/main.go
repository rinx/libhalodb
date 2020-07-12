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
	Open(path string) error
	Put(key, value string) error
	Get(key string) string
	Delete(key string) error
	Size() int64
	Close() error
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

func (h *haloDB) Open(path string) error {
	cspath := C.CString(path)
	defer C.free(unsafe.Pointer(cspath))
	if C.halodb_open(h.thread, cspath) != 0 {
		return fmt.Errorf("failed to open HaloDB instance")
	}
	return nil
}

func (h *haloDB) Put(key, value string) error {
	csKey := C.CString(key)
	defer C.free(unsafe.Pointer(csKey))
	csValue := C.CString(value)
	defer C.free(unsafe.Pointer(csValue))
	if C.halodb_put(h.thread, csKey, csValue) != 0 {
		return fmt.Errorf("failed to put %s to HaloDB instance", key)
	}
	return nil
}

func (h *haloDB) Get(key string) string {
	csKey := C.CString(key)
	defer C.free(unsafe.Pointer(csKey))
	return C.GoString(C.halodb_get(h.thread, csKey))
}

func (h *haloDB) Delete(key string) error {
	csKey := C.CString(key)
	defer C.free(unsafe.Pointer(csKey))
	if C.halodb_delete(h.thread, csKey) != 0 {
		return fmt.Errorf("failed to delete %s from HaloDB instance", key)
	}
	return nil
}

func (h *haloDB) Size() int64 {
	res := C.halodb_size(h.thread)
	return *(*int64)(unsafe.Pointer(&res))
}

func (h *haloDB) Close() error {
	if C.halodb_close(h.thread) != 0 {
		return fmt.Errorf("failed to close HaloDB instance")
	}
	return nil
}

func main() {
	haloDB, err := New()
	if err != nil {
		println(err)
		return
	}

	err = haloDB.Open(".halodb")
	if err != nil {
		fmt.Printf("error: %s", err)
		return
	}
	defer haloDB.Close()

	fmt.Println(fmt.Sprintf("current size: %d", haloDB.Size()))

	err = haloDB.Put("k1", "this is written from Go!")
	if err != nil {
		fmt.Printf("error: %s", err)
		return
	}
	err = haloDB.Put("k2", "this is written from Go! 2")
	if err != nil {
		fmt.Printf("error: %s", err)
		return
	}

	fmt.Println(fmt.Sprintf("current size: %d", haloDB.Size()))

	fmt.Println(fmt.Sprintf("Get k1: %s", haloDB.Get("k1")))
	fmt.Println(fmt.Sprintf("Get k2: %s", haloDB.Get("k2")))

	fmt.Println(fmt.Sprintf("current size: %d", haloDB.Size()))

	err = haloDB.Delete("k1")
	if err != nil {
		fmt.Printf("error: %s", err)
		return
	}

	fmt.Println(fmt.Sprintf("current size: %d", haloDB.Size()))
}
