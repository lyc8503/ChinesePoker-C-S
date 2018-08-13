import socket
import os
import _thread


def clear():
    print("\n" * 100)


global win_flag
win_flag = False

global next_flag
next_flag = False


def in_game_read():
    global win_flag
    global next_flag
    while not win_flag:
        ser_res = str(sock.recv(1024), encoding="utf-8")
        if ser_res[0:9] == "Game Over":
            win_flag = True
        if ser_res == "Error!":
            clear()
            print("Server Internal Error!")
            os.exit(233)
        print(ser_res)
        next_flag = True


print("Chinese Poker Game LAN Version by lyc8503")
# addr = input("Enter Server Address:")
# port = input("Enter Server Port:")
print("Connecting... Please wait!")
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# sock.connect((addr, int(port))
sock.connect(("192.168.1.199", 6666))
sock.send(bytes("pokerClient\n", encoding="utf-8"))
if str(sock.recv(1024), encoding="utf-8") == "OK":
    print("Success!")
else:
    print("Error!")
    os.exit(666)
nick_name = input("Enter your nickname:")
sock.send(bytes(nick_name + "\n", encoding="utf-8"))
sock.send(bytes("hub\n", encoding="utf-8"))
clear()
print("Getting hub...")
print("==========")
print(str(sock.recv(40960), encoding="utf-8"))
print("==========")

loop = True
while loop:
    join = input("Choose the room you want to join:")
    sock.send(bytes("join@" + join + "\n", encoding="utf-8"))
    res = str(sock.recv(4096), encoding="utf-8")
    print(res)
    if res[0:1] == "S":
        loop = False

print("Waiting for start signal...This may take a while.")
stat = str(sock.recv(1024), encoding="utf-8")
if stat == "ready":
    print("Game Start!")

card = str(sock.recv(1024), encoding="utf-8")
print("Your Cards:" + card)

landlord = True
loop = True
while loop:
    pro = input("Do you want to be landlord (y/n)?")
    pro = pro.lower()
    if pro != "y" and pro != "n":
        loop = True
    else:
        loop = False
        if pro == "y":
            landlord = True
        else:
            landlord = False

print("Please wait for other players...")
if landlord:
    sock.send(bytes("T\n", encoding="utf-8"))
else:
    sock.send(bytes("LOL\n", encoding="utf-8"))

res = str(sock.recv(1024), encoding="utf-8")
if res == "landlord":
    print("You get the landlord!")
    res2 = str(sock.recv(1024), encoding="utf-8")
    print(res2)
else:
    print(res)

win_flag = False
next_flag = True

_thread.start_new_thread(in_game_read, ())
while (not win_flag):
    if next_flag:
        cards = input("Your choice(Use space to divide, Leave empty to pass):")
        if cards == "":
            cards = "pass"
        sock.send(bytes(cards + "\n", encoding="utf-8"))
        next_flag = False
