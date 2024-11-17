# == Schema Information
#
# Table name: transaction_types
#
#  id         :bigint           not null, primary key
#  nome       :string
#  user_id    :bigint           not null
#  created_at :datetime         not null
#  updated_at :datetime         not null
#
class TransactionType < ApplicationRecord
  belongs_to :user
end
