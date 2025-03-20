#!/bin/bash

# Define the minimum and maximum number of executions per day
MIN_EXEC_CURVESTAKE=3
MAX_EXEC_CURVESTAKE=15

# Calculate the probability of execution based on the schedule
TOTAL_RUNS_CURVESTAKE=144 # Example: 144 cron runs per day (every 10 minutes)
RANDOM_THRESHOLD=$(( (MAX_EXEC_CURVESTAKE - MIN_EXEC_CURVESTAKE + 1) * 100 / TOTAL_RUNS_CURVESTAKE ))

# Decide randomly whether to execute the task
if [ $((RANDOM % 100)) -lt $RANDOM_THRESHOLD ]; then
    # Place your main task here
    python3 ./update_number.py >> /tmp/curvestake-logfile.log
    echo "Task is executing at $(date)" >> /tmp/curvestake-logfile.log
else
    echo "Task skipped at $(date)" >> /tmp/curvestake-logfile.log
fi

