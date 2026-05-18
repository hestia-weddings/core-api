.PHONY: test

SHELL := /bin/bash

test:
	@bash -c '\
	GREEN="\e[0;32m"; RED="\e[0;31m"; YELLOW="\e[0;33m"; CYAN="\e[0;36m"; BOLD="\e[1m"; RESET="\e[0m"; \
	cleanup() { kill $$TIMER_PID 2>/dev/null; wait $$TIMER_PID 2>/dev/null; printf "\n"; exit 1; }; \
	trap cleanup INT TERM; \
	printf "\n$${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━$${RESET}\n\n"; \
	START=$$(date +%s%N); \
	( while true; do \
		NOW=$$(date +%s%N); \
		ELAPSED=$$(echo "scale=1; ($$NOW - $$START) / 1000000000" | bc); \
		printf "\r$${CYAN}$${BOLD}  🧪 Running test suite... $${ELAPSED}s$${RESET}   "; \
		sleep 0.1; \
	done ) & \
	TIMER_PID=$$!; \
	RESULT=$$(mvn clean test -DskipTests=false -Dspring.profiles.active=test 2>&1); \
	kill $$TIMER_PID 2>/dev/null; \
	wait $$TIMER_PID 2>/dev/null; \
	END=$$(date +%s%N); \
	TOTAL=$$(echo "scale=1; ($$END - $$START) / 1000000000" | bc); \
	printf "\r$${CYAN}$${BOLD}  🧪 Completed in $${TOTAL}s$${RESET}              \n\n"; \
	ERRORS=$$(echo "$$RESULT" | grep -E "ERROR.*<<<" | grep -v "WARNING"); \
	PASSED=$$(echo "$$RESULT" | grep -E "Tests run:.*Time elapsed" | grep -v "Errors: [1-9]\|Failures: [1-9]" | sed "s/\[INFO\] //" | sed "s/ -- in /|/" | awk -F"|" "{print \$$2}" | grep -v "^$$"); \
	SUMMARY=$$(echo "$$RESULT" | grep "Tests run:" | tail -1); \
	BUILD=$$(echo "$$RESULT" | grep "BUILD"); \
	printf "$${GREEN}$${BOLD}  Passed:$${RESET}\n"; \
	echo "$$PASSED" | while read -r line; do \
		if [ -n "$$line" ]; then \
			printf "$${GREEN}    ✓ $$line$${RESET}\n"; \
		fi; \
	done; \
	printf "\n"; \
	if [ -n "$$ERRORS" ]; then \
		printf "$${RED}$${BOLD}  Failed:$${RESET}\n"; \
		echo "$$ERRORS" | sed "s/\[ERROR\] //" | while read -r line; do \
			printf "$${RED}    ✗ $$line$${RESET}\n"; \
		done; \
		printf "\n"; \
	fi; \
	if echo "$$SUMMARY" | grep -q "Failures: 0, Errors: 0"; then \
		printf "$${GREEN}$${BOLD}  ✅ $$SUMMARY$${RESET}\n"; \
	else \
		printf "$${RED}$${BOLD}  ❌ $$SUMMARY$${RESET}\n"; \
	fi; \
	printf "\n"; \
	if [ -f target/site/jacoco/jacoco.csv ]; then \
		COV=$$(awk -F"," "NR>1{mi+=\$$4;ci+=\$$5;mb+=\$$6;cb+=\$$7} END{printf \"%.0f\", ci/(mi+ci)*100}" target/site/jacoco/jacoco.csv); \
		BR=$$(awk -F"," "NR>1{mi+=\$$6;ci+=\$$7} END{printf \"%.0f\", ci/(mi+ci)*100}" target/site/jacoco/jacoco.csv); \
		printf "$${YELLOW}$${BOLD}  📊 Coverage$${RESET}\n"; \
		printf "$${YELLOW}     Instructions: $${COV}%%  |  Branches: $${BR}%%$${RESET}\n"; \
	fi; \
	printf "\n$${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━$${RESET}\n"; \
	if echo "$$BUILD" | grep -q "SUCCESS"; then \
		printf "$${GREEN}$${BOLD}  🎉 All good!$${RESET}\n\n"; \
	else \
		printf "$${RED}$${BOLD}  💥 Tests failed. Check logs above.$${RESET}\n\n"; \
	fi'
