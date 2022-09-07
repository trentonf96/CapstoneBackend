#!/usr/bin/python

import sys
from os import path
from pydub import AudioSegment

src = sys.argv[1] + sys.argv[2]
dst = sys.argv[1] + sys.argv[3]

sound = AudioSegment.from_file(src, codec="opus")
sound.export(dst, format="wav")
