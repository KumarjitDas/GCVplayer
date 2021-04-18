import cv2
import Log
import GestureDetector


def Main():
    detection_list = ['Give a command', 'Previous',  'Next', 'Give a command with index finger',
                      'Play/Pause',     'Volume up', 'Volume down', '+10 seconds', '-10 seconds'
                     ]
    default_detection = detection_list[0]

    webcam = cv2.VideoCapture(0)
    window_shape = (int(webcam.get(3)), int(webcam.get(4)))

    logger = Log.Logger()
    gesture = GestureDetector.Hand(window_shape)

    previous_detection = 0

    file = open('__temp__', 'w', 1)

    while webcam.isOpened():

        return_value, frame = webcam.read()
        if logger.check(return_value): break

        frame = cv2.flip(frame, 1)

        hand_detected, detection = gesture.detect(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))
        if hand_detected:
            if (detection != previous_detection or
                detection == gesture.VOLUME_UP or detection == gesture.VOLUME_DOWN):
                file.write(str(detection) + '\n')
                file.flush()
                # print(detection)

            previous_detection = detection
            default_detection = detection_list[detection]

            if detection == gesture.MAIN_COMMAND:
                index = 0
                while index <= 40:
                    point = (gesture.hand.landmark_list[index], gesture.hand.landmark_list[index + 1])
                    if index == 8 or index == 16:
                        cv2.circle(frame, point, 4, (255, 0, 0), 2)
                    else:
                        cv2.circle(frame, point, 4, (0, 255, 0), 2)

                    index += 8

            elif detection == gesture.NEXT or detection == gesture.PREVIOUS:
                point = (gesture.hand.landmark_list[0], gesture.hand.landmark_list[1])
                cv2.circle(frame, point, 4, (0, 255, 0), 2)
                point = (gesture.hand.landmark_list[8], gesture.hand.landmark_list[9])
                cv2.circle(frame, point, 6, (0, 0, 255), 2)

            elif detection == gesture.INDEX_COMMAND:
                point = (gesture.hand.landmark_list[16], gesture.hand.landmark_list[17])
                cv2.circle(frame, point, 4, (255, 0, 0), 2)
                point = (gesture.hand.landmark_list[8], gesture.hand.landmark_list[9])
                cv2.circle(frame, point, 4, (0, 255, 0), 2)

            elif detection == gesture.PLAY_PAUSE:
                point = (gesture.hand.landmark_list[16], gesture.hand.landmark_list[17])
                cv2.circle(frame, point, 6, (0, 0, 255), 2)
                point = (gesture.hand.landmark_list[8], gesture.hand.landmark_list[9])
                cv2.circle(frame, point, 6, (0, 0, 255), 2)

            else:
                point = (gesture.hand.landmark_list[16], gesture.hand.landmark_list[17])
                cv2.circle(frame, point, 6, (0, 0, 255), 2)
                point = (gesture.hand.landmark_list[8], gesture.hand.landmark_list[9])
                cv2.circle(frame, point, 4, (0, 255, 0), 2)


        cv2.putText(frame, default_detection, (20, 30),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.7, (10, 10, 10), 2
                   )
        cv2.imshow('Hand Gesture Detector', frame)

        if cv2.waitKey(1) == 27: 
            file.write('X')
            file.flush()
            file.close()
            break

    webcam.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    Main()
