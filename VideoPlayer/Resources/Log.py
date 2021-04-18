class Logger:
    def __init__(self):
        self.warning_count = 0

    def check(self, returnValue) -> bool:
        if not returnValue:
            if self.warning_count >= 10:
                print('Error: Could not read webcam frame. Exiting.')
                return True

            print('Warning: Webcam frame could not be read properly. Trying...')
            self.warning_count += 1

        return False
