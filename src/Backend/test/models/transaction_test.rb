# == Schema Information
#
# Table name: transactions
#
#  id                  :bigint           not null, primary key
#  valor               :float
#  data                :datetime
#  observacao          :string
#  transaction_type_id :bigint           not null
#  user_id             :bigint           not null
#  created_at          :datetime         not null
#  updated_at          :datetime         not null
#
require "test_helper"

class TransactionTest < ActiveSupport::TestCase
  # test "the truth" do
  #   assert true
  # end
end
