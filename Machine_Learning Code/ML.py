#import numpy as np
import sys
import math as math
import World
import threading
import time
import random

grid = World.grid
actions = World.actions
policy_sign = ["^", "<", "v", ">"]
states = []
optimalRouteFlag = False
listOptimalMoves = []


device = str(sys.argv[1])

current = World.start
walls = World.walls
goal = World.goal   
pit = World.pit 


if ("draginotest" in sys.argv[1]):
    pit.append((0,0));

if ("dragino2" in sys.argv[1]):
    pit.append((2,0));

if ("dragino3" in sys.argv[1]):
    pit.append((4,0));

if ("dragino4" in sys.argv[1]):
    pit.append((0,3));
       
if ("dragino5" in sys.argv[1]):
    pit.append((2,3));

if ("dragino6" in sys.argv[1]):
    pit.append((4,3));
    
if ("dragino7" in sys.argv[1]):
    pit.append((0,5));
    
if ("dragino8" in sys.argv[1]):
    pit.append((2,5));

if ("dragino9" in sys.argv[1]):
    pit.append((4,5));
    
if ("dragino10" in sys.argv[1]):
    pit.append((0,7));

if ("dragino11" in sys.argv[1]):
    pit.append((1,7));

if ("dragino12" in sys.argv[1]):
    pit.append((4,7));

iter = 1

Q = {}
discount = 0.8
alpha = 1
score = 1
move_reward = -0.04
goal_reward = 1
pit_reward = -1
move_pass = 0.8
move_fail = 0.1
move_action = [-1, 0, 1]
epsilon = 0.1
episodes = 10000

def init():
    for i in range(World.x):
        for j in range(World.y):
            if (i, j) in walls:        
                continue
            states.append((i, j))


    for state in states:
        temp = {}
        for action in actions:
            if state in pit:
                temp[action] = pit_reward
            elif state in goal:
                temp[action] = goal_reward
            else:
                temp[action] = 0.1
        Q[state] = temp

def move(action):
    global current, score
    s = current
    (curr_x, curr_y) = current

    if action == actions[1]:
        current = (curr_x-1 if curr_x-1 >= 0 else curr_x, curr_y)
    elif action == actions[3]:
        current = (curr_x+1 if curr_x+1 < World.x else curr_x, curr_y)
    elif action == actions[0]:
        current = (curr_x, curr_y-1 if curr_y-1 >= 0 else curr_y)
    elif action == actions[2]:
        current = (curr_x, curr_y+1 if curr_y+1 < World.y else curr_y)

    if current in walls:
        current = s
    elif current in goal:
        if (optimalRouteFlag):
            listOptimalMoves.append(current)
            print(str(sys.argv[1]),listOptimalMoves)
            exit(0)
        else:
            World.restart = True
    elif current in pit:
        World.restart = True

    World.move_bot(current[0], current[1])
    r = move_reward

    score += r
    s2 = current
    return s, action, r, s2


def max_q(state):
    q_val = None
    act = None
    for a, q in Q[state].items():
        if q_val is None or q > q_val:
            q_val = q
            act = a
    return act, q_val


def update_q(s, a, alpha, new_q):
    Q[s][a] *= (1 - alpha)
    Q[s][a] += (alpha * new_q)

def random_action(act):
    random.seed(a=None)
    r = random.random()
    other_actions = []
    for a in actions:
        if a !=act:
            other_actions.append(a)
    if r >= 1 - epsilon:
        r2 = random.randint(0, 2)
        return other_actions[r2]

    else:
        return act


def q_learn():
    global iter, alpha, discount, current, score, epsilon, episodes, optimalRouteFlag, listOptimalMoves
    init()
    while iter != episodes:
        (max_act, max_val) = max_q(current)
        if (iter > 2000  and current == World.start) or (optimalRouteFlag):
            optimalRouteFlag = True
            listOptimalMoves.append(current)
        (s, a, reward, s2) = move(random_action(max_act))
        
        (max_act2, max_val2) = max_q(s2)
        update_q(s, a, alpha, reward + discount*max_val2)
        
        iter += 1
        if World.restart is True:
            current = World.start
            World.move_bot(current[0], current[1])
            World.restart = False
            World.restart_game()
            alpha = pow(iter, -0.1)
            score = 1

        epsilon = 0.0
        discount = 0.8

def main():
    World.begin()
    q_learn()
if __name__== "__main__":
    main()