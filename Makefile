.PHONY: test

SHELL := /bin/bash

test:
	@bash -c '\
	use_color=0; use_spinner=0; \
	if [ -t 1 ] && command -v tput >/dev/null && [ -z "$$NO_COLOR" ] && [ "$$TERM" != "dumb" ]; then \
		BOLD=$$(tput bold); DIM=$$(tput dim); RESET=$$(tput sgr0); \
		RED=$$(tput setaf 1); BLUE=$$(tput setaf 4); GREEN=$$(tput setaf 2); YELLOW=$$(tput setaf 3); \
		use_color=1; \
	else \
		BOLD=""; DIM=""; RESET=""; RED=""; BLUE=""; GREEN=""; YELLOW=""; \
	fi; \
	if [ -t 1 ] && [ "$$TERM" != "dumb" ] && [ -z "$$CI" ] && [ -z "$$GITHUB_ACTIONS" ] && [ -z "$$NO_SPINNER" ]; then use_spinner=1; fi; \
	cols=$$(tput cols 2>/dev/null || echo 80); \
	line() { printf "%*s\n" "$$cols" "" | tr " " "-"; }; \
	start=$$(date +%s%N); \
	tmp=$$(mktemp); \
	cleanup() { [ -n "$$pid" ] && kill $$pid 2>/dev/null; rm -f "$$tmp"; printf "\n"; exit 1; }; \
	trap cleanup INT TERM; \
	printf "\n$${DIM}"; line; printf "$${RESET}\n"; \
	printf "$${BOLD}  == Test run ==$${RESET}\n"; \
	if [ "$${VERBOSE:-0}" = "1" ]; then \
		set -o pipefail; \
		mvn clean test -DskipTests=false -Dspring.profiles.active=test 2>&1 | tee "$$tmp"; \
		exit_code=$${PIPESTATUS[0]}; \
	else \
		if [ "$$use_spinner" -eq 1 ]; then \
			(mvn clean test -DskipTests=false -Dspring.profiles.active=test > "$$tmp" 2>&1) & \
			pid=$$!; \
			spinner="|/-\\"; i=0; \
			while kill -0 $$pid 2>/dev/null; do \
				now=$$(date +%s%N); \
				elapsed=$$(echo "scale=1; ($$now - $$start) / 1000000000" | bc); \
				frame=$$(printf "%s" "$$spinner" | cut -c $$((i % 4 + 1))); \
				printf "\r$${BOLD}  [$${BLUE}RUN$${RESET}] $$frame Running tests... $${DIM}%ss$${RESET} " "$$elapsed"; \
				i=$$((i+1)); \
				sleep 0.1; \
			done; \
			wait $$pid; exit_code=$$?; \
		else \
			printf "  [$${BLUE}RUN$${RESET}] Running tests...\n"; \
			mvn clean test -DskipTests=false -Dspring.profiles.active=test > "$$tmp" 2>&1; \
			exit_code=$$?; \
		fi; \
	fi; \
	end=$$(date +%s%N); \
	total=$$(echo "scale=1; ($$end - $$start) / 1000000000" | bc); \
	if [ "$$exit_code" -eq 0 ]; then done_tag="[$${BLUE}DONE$${RESET}]"; else done_tag="[$${RED}DONE$${RESET}]"; fi; \
	printf "\r$${BOLD}  $$done_tag Completed in $${total}s$${RESET}                        \n"; \
	result=$$(cat "$$tmp"); \
	rm -f "$$tmp"; \
	summary=$$(echo "$$result" | grep "Tests run:" | tail -1 | sed "s/\[INFO\] //"); \
	passed=$$(echo "$$result" | grep -E "Tests run:.*Time elapsed" | grep -v "Errors: [1-9]\|Failures: [1-9]" | sed "s/\[INFO\] //" | sed "s/ -- in /|/" | awk -F"|" "{print \$$2}" | grep -v "^$$"); \
	errors=$$(echo "$$result" | grep -E "ERROR.*<<<" | grep -v "WARNING"); \
	if [ -z "$$summary" ]; then summary="No test summary found"; fi; \
	if [ "$$exit_code" -eq 0 ]; then result_label="[$${GREEN}PASS$${RESET}]"; else result_label="[$${RED}FAIL$${RESET}]"; fi; \
	printf "\n$${BOLD}  == Summary ==$${RESET}\n"; \
	printf "  %-16s %s\n" "- Result" "$$result_label"; \
	printf "  %-16s %ss\n" "- Duration" "$$total"; \
	printf "  %-16s %s\n" "- Tests" "$$summary"; \
	if [ -n "$$passed" ]; then \
		printf "\n$${BOLD}  == Suites ==$${RESET}\n"; \
		echo "$$passed" | while read -r line; do \
			if [ -n "$$line" ]; then \
				printf "    [$${GREEN}CASE$${RESET}] $$line\n"; \
			fi; \
		done; \
	fi; \
	if [ -n "$$errors" ]; then \
		printf "\n$${BOLD}  == $${RED}Failures$${RESET}$${BOLD} ==$${RESET}\n"; \
		echo "$$errors" | sed "s/\[ERROR\] //" | while read -r line; do \
			printf "    [$${RED}FAIL$${RESET}] $$line\n"; \
		done; \
	fi; \
	if [ -f target/site/jacoco/jacoco.csv ]; then \
		cov=$$(awk -F"," "NR>1{mi+=\$$4;ci+=\$$5;mb+=\$$6;cb+=\$$7} END{printf \"%.0f\", ci/(mi+ci)*100}" target/site/jacoco/jacoco.csv); \
		br=$$(awk -F"," "NR>1{mi+=\$$6;ci+=\$$7} END{printf \"%.0f\", ci/(mi+ci)*100}" target/site/jacoco/jacoco.csv); \
		printf "\n$${BOLD}  == Coverage ==$${RESET}\n"; \
		printf "    [$${YELLOW}COV$${RESET}] Instructions: $${cov}%%  |  Branches: $${br}%%\n"; \
	fi; \
	if [ "$$exit_code" -ne 0 ]; then \
		printf "\n$${BOLD}  == $${RED}Logs$${RESET}$${BOLD} ==$${RESET}\n"; \
		printf "$${RED}  Maven output (last 200 lines)$${RESET}\n"; \
		echo "$$result" | tail -200; \
	fi; \
	printf "\n$${DIM}"; line; printf "$${RESET}\n"; \
	if [ "$$exit_code" -eq 0 ]; then \
		printf "$${BOLD}  [$${BLUE}PASS$${RESET}] All good!$${RESET}\n\n"; \
	else \
		printf "$${BOLD}  [$${RED}FAIL$${RESET}$${BOLD}] $${RED}Tests failed. Check logs above.$${RESET}\n\n"; \
	fi; \
	exit $$exit_code'
