import LandMarkDetector
import math


class Hand:
    def __init__(self, window_shape: tuple):
        self.MAIN_COMMAND  = 0
        self.PREVIOUS      = 1
        self.NEXT          = 2

        self.INDEX_COMMAND = 3
        self.PLAY_PAUSE    = 4
        self.VOLUME_UP     = 5
        self.VOLUME_DOWN   = 6
        self.FORWARD       = 7
        self.BACKWARD      = 8

        self.window_shape = window_shape
        self.hand         = LandMarkDetector.Hand()

        self.set_index_position = True
        self.index_position     = None

        self.offset   = 0
        self.distance = 0

    def detect(self, image) -> (bool, int):
        detection = self.MAIN_COMMAND
        hand_detected = self.hand.detect(image)

        if hand_detected:
            index_down  = self.hand.landmark_list[17] >= self.hand.landmark_list[13]
            others_down = (self.hand.landmark_list[25] >= self.hand.landmark_list[21] and
                           self.hand.landmark_list[33] >= self.hand.landmark_list[29] and
                           self.hand.landmark_list[41] >= self.hand.landmark_list[37])

            if index_down and others_down:
                if self.hand.landmark_list[8] == self.hand.bound[0]: detection = self.PREVIOUS
                if self.hand.landmark_list[8] == self.hand.bound[2]: detection = self.NEXT
                self.set_index_position = True

            elif others_down:
                detection = self.INDEX_COMMAND

                if self.set_index_position:
                    thumb_position      = (self.hand.landmark_list[8], self.hand.landmark_list[9])
                    self.index_position = (self.hand.landmark_list[16], self.hand.landmark_list[17])

                    x = thumb_position[0] - self.index_position[0]
                    y = thumb_position[1] - self.index_position[1]
                    self.distance = math.sqrt((x * x) + (y * y))

                    if self.index_position[0] >= thumb_position[0]:
                        self.offset = self.hand.bound[2] - self.index_position[0]
                    else:
                        self.offset = self.index_position[0] - self.hand.bound[0]

                    self.set_index_position = False

                else:
                    if   self.hand.landmark_list[16] >= (self.index_position[0] + self.offset):
                        detection = self.FORWARD
                    elif self.hand.landmark_list[16] <= (self.index_position[0] - self.offset):
                        detection = self.BACKWARD
                    elif self.hand.landmark_list[17] >= (self.index_position[1] + self.offset):
                        detection = self.VOLUME_DOWN
                    elif self.hand.landmark_list[17] <= (self.index_position[1] - self.offset):
                        detection = self.VOLUME_UP
                    else:
                        x = self.hand.landmark_list[8] - self.hand.landmark_list[16]
                        y = self.hand.landmark_list[9] - self.hand.landmark_list[17]

                        if math.sqrt((x * x) + (y * y)) < (self.distance / 2):
                            detection = self.PLAY_PAUSE

            else: 
                self.set_index_position = True
                self.index_position = None

        else: 
            self.set_index_position = True
            self.index_position = None

        return hand_detected, detection
