[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/central-university-dev/backend_academy_2024_project_3-java-qwatro2/blob/master/README.md)
[![pt-br](https://img.shields.io/badge/lang-ru-green.svg)](https://github.com/central-university-dev/backend_academy_2024_project_3-java-qwatro2/blob/master/README.ru.md)

# Fractal Flame
<img src=images/title.png width="400" height="400"  alt="title-image"/>

## How to use?
You can run it with the `--help` flag and see the help
### Parameters
|       Parameter        |        Type        |                    Description                    |     Default value      |
|:----------------------:|:------------------:|:-------------------------------------------------:|:----------------------:|
|     `--n-samples`      |      int > 0       |                 number of samples                 |          1000          |
|    `--n-iterations`    |      int > 0       |          number of iterations per sample          |         100000         |
|    `--n-symmetries`    |      int > 0       |           number of circular symmetries           | 1 (without symmetries) |
| `--n-transformations`  |      int > 0       |         number of affine transformations          |           10           |
|     `--n-threads`      |      int > 0       |     number of threads in multithreading mode      |  null (no threading)   |
|       `--width`        |      int > 0       |             width of resulting image              |          900           |
|       `--height`       |      int > 0       |             height of resulting image             |          900           |
|        `--seed`        |        long        |             seed of random generation             |     null (no seed)     |
|        `--path`        |       string       |            path to file to save image             |      ./result.png      |
|       `--format`       | png \| jpeg \| bmp |               format of saved image               |          png           |
|  `--generation-order`  | ordered \| random  | order of application of nonlinear transformations |        ordered         |
| `--add-transformation` |       string       |    adding nonlinear transformation to the list    |           -            |

### `--add-transformation`
+ takes one of the strings: `disk`, `eyefish`, `handkerchief`, `hearth`, `horseshoe`, `polar`, `sinus`, `sphere`, `swirl`
+ adds the specified transformation to the list of nonlinear transformations
+ can be used any number of times, but not less than one

### Пример использования
```bash
java -jar ./fractals.jar --add-transformation disk --add-transformation swirl --path ./images/flame.png --seed 142857
```

## What's going on inside?
The managing entity is `FractalsApp` inherited from `AbstractFractalsApp` \
The `void run(String[] args)` method builds the following pipeline:
1. Get the `ParamsGetter` entity
2. Use it to get the `Params` parameters
3. Check the correctness of the parameters
   1. If incorrect, exit
4. Get the `ImageRenderer` entity
5. Use it to generate the `PixelImage` image
6. Get the `ImageCorrector` entity
7. Correct the image obtained in step 5
8. Get the `ImageSaver` entity
9. Save the image
10. Check the correctness of saving

### A little bit about architecture
The `AbstractFractalsApp` abstraction captures only the pipeline specified above,
all other methods are abstract, return an interface type and must be overridden. \
This allows the user to inherit from this class and decide for themselves which implementations of these interfaces they need

## Time measurements
I conducted 5 measurements with different numbers of threads used, all other parameters being equal:
### Measurement #1
|           Версия            | Время в миллисекундах |
|:---------------------------:|:---------------------:|
|   SingleThreaded version    |         3137          |
|  MultiThreaded (1 thread)   |         2568          |
|  MultiThreaded (2 threads)  |         2703          |
|  MultiThreaded (5 threads)  |         1101          |
| MultiThreaded (10 threads)  |         1061          |
| MultiThreaded (100 threads) |         1044          |

### Measurement #2
|           Версия            | Время в миллисекундах |
|:---------------------------:|:---------------------:|
|   SingleThreaded version    |         2766          |
|  MultiThreaded (1 thread)   |         2501          |
|  MultiThreaded (2 threads)  |         2654          |
|  MultiThreaded (5 threads)  |         1137          |
| MultiThreaded (10 threads)  |          973          |
| MultiThreaded (100 threads) |          947          |

### Measurement #3
|           Версия            | Время в миллисекундах |
|:---------------------------:|:---------------------:|
|   SingleThreaded version    |         3136          |
|  MultiThreaded (1 thread)   |         2735          |
|  MultiThreaded (2 threads)  |         2945          |
|  MultiThreaded (5 threads)  |         1145          |
| MultiThreaded (10 threads)  |          975          |
| MultiThreaded (100 threads) |         1044          |

### Measurement #4
|           Версия            | Время в миллисекундах |
|:---------------------------:|:---------------------:|
|   SingleThreaded version    |         2927          |
|  MultiThreaded (1 thread)   |         2439          |
|  MultiThreaded (2 threads)  |         2439          |
|  MultiThreaded (5 threads)  |         1125          |
| MultiThreaded (10 threads)  |         1002          |
| MultiThreaded (100 threads) |         1004          |

### Measurement #5
|           Версия            | Время в миллисекундах |
|:---------------------------:|:---------------------:|
|   SingleThreaded version    |         3213          |
|  MultiThreaded (1 thread)   |         2639          |
|  MultiThreaded (2 threads)  |         2438          |
|  MultiThreaded (5 threads)  |         1155          |
| MultiThreaded (10 threads)  |         1095          |
| MultiThreaded (100 threads) |         1079          |

### Average time for 5 measurements
|           Версия            | Время в миллисекундах |
|:---------------------------:|:---------------------:|
|   SingleThreaded version    |        3035.8         |
|  MultiThreaded (1 thread)   |        2576.4         |
|  MultiThreaded (2 threads)  |        2635.8         |
|  MultiThreaded (5 threads)  |        1132.6         |
| MultiThreaded (10 threads)  |        1021.2         |
| MultiThreaded (100 threads) |        1023.6         |

### Conclusions and patterns
+ single-threaded version of the program works slower than multithreaded with 1 thread
+ version with 2 threads on average works longer than version with 1 thread, but still faster than single-threaded
+ version with 5 threads works 3 times faster than single-threaded
+ version with 10 threads does not provide such a big increase in speed
+ version with 100 threads on average works a little worse than version with 10.
  Most likely, this is due to the fact that too much time is spent waiting for resources and switching contexts

## Likes
<img src=images/likes/on_title.png width="500" height="800"  alt="title-image"/>
<img src=images/likes/ten_images.png width="400" height="800"  alt="title-image"/>

## Beautiful pictures
<img src=images/fractals/1.png width="400" height="400"  alt="title-image"/>
<img src=images/fractals/2.png width="400" height="400"  alt="title-image"/>
<img src=images/fractals/3.png width="400" height="400"  alt="title-image"/>
<img src=images/fractals/4.png width="400" height="400"  alt="title-image"/>
<img src=images/fractals/5.png width="400" height="400"  alt="title-image"/>
<img src=images/fractals/6.png width="400" height="400"  alt="title-image"/>
<img src=images/fractals/7.png width="400" height="400"  alt="title-image"/>
<img src=images/fractals/8.png width="400" height="400"  alt="title-image"/>
<img src=images/fractals/9.png width="400" height="400"  alt="title-image"/>
<img src=images/fractals/10.png width="400" height="400"  alt="title-image"/>



