FROM maven:3.8-openjdk-11

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Install dependencies
RUN mvn dependency:go-offline

# Set default command
CMD ["mvn", "test"] 