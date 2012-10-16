#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("kmeans")

  results = RDD_SLICES.product(NUM_REDUCE_TASKS, KMEANS_NUM_POINTS, KMEANS_DIMENSION).map do |args|
    result = run("spark.perf.KMeans", args)
    puts result
    [args, result]
  end

end