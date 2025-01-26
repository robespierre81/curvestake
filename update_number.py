#!/usr/bin/env python3
import subprocess
from datetime import datetime
import os


script_dir = os.path.dirname(os.path.abspath(__file__))
os.chdir(script_dir)

def read_number():
    with open('number.txt', 'r') as f:
        return int(f.read().strip())

def write_number(num):
    with open('number.txt', 'w') as f:
        f.write(str(num))

def git_commit():
    # Stage the changes
    subprocess.run(['git', 'add', 'number.txt'])
    
    # Create commit with current date
    date = datetime.now().strftime('%Y-%m-%d')
    commit_message = f"Update number: {date}"
    subprocess.run(['git', 'commit', '-m', commit_message])

def git_push():
    # Push the changes to the remote repository
    try:
        subprocess.run(['git', 'push'], check=True)
        print("Pushed to remote repository successfully.")
    except subprocess.CalledProcessError as e:
        print(f"Error during git push: {e}")
        exit(1)

def main():
    try:
        current_number = read_number()
        new_number = current_number + 1
        write_number(new_number)
        git_commit()
        git_push()
    except Exception as e:
        print(f"Error: {str(e)}")
        exit(1)

if __name__ == "__main__":
    main() 