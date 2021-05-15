FROM openjdk:8

# Install.
RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update -y && \
  apt-get upgrade -y && \
  apt-get install -y build-essential && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget maven && \
  apt-get install -y net-tools iputils-ping && \
  rm -rf /var/lib/apt/lists/*

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Installing Common
RUN \
  git clone https://github.com/computacao-ufcg/eureca-common.git && \
  (cd eureca-common && git checkout $EURECA_COMMON_BRANCH && mvn install -Dmaven.test.skip=true) 

# Downloading Authentication service
RUN \
  git clone https://github.com/computacao-ufcg/eureca-as.git && \
  (cd eureca-as && git checkout $EURECA_AS_BRANCH && mvn install -Dmaven.test.skip=true )

# Generates the build number based on the commit checksum
RUN \
    (cd eureca-common && common_build_number=$(git rev-parse --short 'HEAD') && \
    cd ../eureca-as && as_build_number=$(git rev-parse --short 'HEAD') && \
    echo "build_number=as-$as_build_number-common-$common_build_number" > build)

WORKDIR /root/eureca-as

CMD \
  mvn spring-boot:run -X > log.out 2> log.err