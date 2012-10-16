#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("simplesum")

  results = RDD_SLICES.product(NUM_REDUCE_TASKS).map do |args|
    result = run("spark.perf.SimpleSum", args)
    puts result
    [args, result]
  end

end