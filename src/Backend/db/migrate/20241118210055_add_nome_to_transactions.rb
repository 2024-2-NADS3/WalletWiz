class AddNomeToTransactions < ActiveRecord::Migration[7.2]
  def change
    add_column :transactions, :nome, :string
  end
end
