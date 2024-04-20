.PHONY: tidy

tidy:
ifeq ($(OS),Windows_NT)
	del /q /f /s target\*.*
	rmdir /s /q target\*
	del /q /f /s receipts\*.*
	rmdir /s /q receipts\*
else
	rm -rf target/*
	rm -rf receipts/*
endif
