#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("logreg")

  results = RDD_SLICES.product(NUM_REDUCE_TASKS, LR_NUM_POINTS, LR_DIMENSION).map do |args|
    result = run("spark.perf.LogReg", args)
    puts result
    [args, result]
  end
  
end

