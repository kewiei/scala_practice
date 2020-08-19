import pandas as pd
import numpy
a = numpy.random.randint(500,size = 1000)
a = pd.DataFrame(a)
a.to_csv('data.csv',index=False)