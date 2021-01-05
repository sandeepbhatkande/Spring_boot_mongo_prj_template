function() {
    var env = karate.env; // get java system property 'karate.env'

    karate.log('karate.env selected environment was:', env);

    var config = {
        HOST: '#KARATE_HOST_URL#'
    };

    karate.configure('readTimeout', 300000);

    return config;
}