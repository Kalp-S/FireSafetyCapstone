import os

grid = []
global goal
actions = ["up", "left", "down", "right"]
cell_score_min = -0.2
cell_score_max = 0.2
filename =  "/home/kalp/Desktop/ML/Q-Maps/custom_map_2.txt"
ins = open(filename,"r")
for line in ins:
    number_strings = line.split()
    numbers = [int(n) for n in number_strings]
    grid.append(numbers)
(x, y) = (len(grid[0]), len(grid))
walls = []
start = ()
specials = []
pit = []
goal=[]
for i in range(y):
    for j in range(x):
        if grid[i][j] == 1:
            walls.append((j, i))
        if grid[i][j] == 2:
            start = (j, i)
        if grid[i][j] == 3:
            specials.append((j, i, "green", 1))
            goal.append((j, i))
        if grid[i][j] == 4:
            specials.append((j, i, "red", -1))
            pit.append((j, i))
player = start
tri_objects = {}
text_objects = {}
flag = True
restart = False

def move_bot(new_x, new_y):
    global player, x, y, score

    if (new_x >= 0) and (new_x < x) and (new_y >= 0) and (new_y < y) and not ((new_x, new_y) in walls):
        player = (new_x, new_y)

def restart_game():
    global player, score, restart
    player = (0, y-1)
    score = 1
    restart = False

def begin():
    global flag
    flag = True