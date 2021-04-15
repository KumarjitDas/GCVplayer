import numpy
import mediapipe

class Hand:

    def __init__(self):
        self.bound: numpy.ndarray = numpy.zeros(4, dtype=numpy.int32)
        self.landmark_list: numpy.ndarray = numpy.zeros(42, dtype=numpy.int32)
        self.detector = mediapipe.solutions.hands.Hands(max_num_hands=1,
                                                        min_detection_confidence=0.7,
                                                        min_tracking_confidence=0.7
                                                       )

    def detect(self, image) -> bool:
        height, width, _ = image.shape
        hand_landmarks = self.detector.process(image).multi_hand_landmarks

        if hand_landmarks:
            x1, y1, x2, y2 = width, height, -width, -height

            for landmarks in hand_landmarks:
                index = 0
                for _, landmark in enumerate(landmarks.landmark):
                    x, y = int(landmark.x * width), int(landmark.y * height)
                    self.landmark_list[index] = x
                    index += 1
                    self.landmark_list[index] = y
                    index += 1

                    if x < x1: x1 = x
                    if x > x2: x2 = x
                    if y < y1: y1 = y
                    if y > y2: y2 = y

                self.bound[0] = x1
                self.bound[1] = y1
                self.bound[2] = x2
                self.bound[3] = y2

            return True

        return False
