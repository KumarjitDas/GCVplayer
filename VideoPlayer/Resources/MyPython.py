import time

with open('__temp__', 'w', 1) as file:
    for i in range(0, 100):
        file.write(str((i + 1) % 10) + '\n')
        file.flush()

        time.sleep(2.0)

    file.write('X')
    file.flush()
